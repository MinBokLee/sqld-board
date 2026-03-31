package com.sqld_board.sqld.service.sign;

import com.sqld_board.sqld.dto.request.sign.SignInMemberReq;
import com.sqld_board.sqld.dto.request.sign.SignUpMemberReq;
import com.sqld_board.sqld.dto.response.member.MemberSimpleInfoRes;
import com.sqld_board.sqld.dto.response.sign.SignInResponseDto;
import com.sqld_board.sqld.dto.response.sign.SignInResult;
import com.sqld_board.sqld.exception.MessageType;
import com.sqld_board.sqld.exception.common.MemberNotFoundException;
import com.sqld_board.sqld.exception.common.SignInFailureException;
import com.sqld_board.sqld.exception.emailVerification.ExistMailException;
import com.sqld_board.sqld.exception.member.ExistMemberException;
import com.sqld_board.sqld.exception.member.NotMatchUserException;
import com.sqld_board.sqld.handler.JwtHandler;
import com.sqld_board.sqld.mapper.BoardMapper;
import com.sqld_board.sqld.mapper.EmailVerificationMapper;
import com.sqld_board.sqld.mapper.MemberMapper;
import com.sqld_board.sqld.model.member.EmailVerification;
import com.sqld_board.sqld.model.member.MemberInfo;
import com.sqld_board.sqld.model.member.Role;
import com.sqld_board.sqld.model.token.RefreshToken;

import com.sqld_board.sqld.service.token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {

    private final MemberMapper memberMapper;
    private final BoardMapper boardMapper;
    private final EmailVerificationMapper emailVerificationMapper;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final JwtHandler jwtHandler;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token.expiration-seconds}")
    private long accessTokenExpiration;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Transactional(readOnly = true)
    public Optional<MemberSimpleInfoRes> readMemberSimpleInfo(String memberId) {

        //1. 회원 정보 조회
        Optional<MemberSimpleInfoRes> info = memberMapper.readMemberSimpleInfo(memberId);

        //2. 존재하지 않는 정보면 예외처리.
        if(info.isEmpty()){
            throw new MemberNotFoundException();
        }
         return info;
    }

    /**
     * 회원 탈퇴 (단건)
     * @param memberId
     * @return
     */
    @Override
    @Transactional
    public MessageType deleteByMemberId(String memberId) {
        // 현재 로그인한 사용자의 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 비로그인 사용자 차단
        if(authentication == null || "anonymousUser".equals(authentication.getName())) {
            throw new NotMatchUserException();
        }
        String currentUserId = authentication.getName(); //JWT 토큰에 담긴 사용자의 ID(username)

        // 관리자 여부 확인
        boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // 본인도 아니고, 관리자 권한이 없으면 차단
        if(!currentUserId.equals(memberId) && !isAdmin)   {
            throw new NotMatchUserException();
        }

        // 회원 탈퇴 사용자가 작성한 글과 댓글의 주인을 unknown으로 변경
        boardMapper.anonymizePosts(memberId);
        boardMapper.anonymizeComments(memberId);


        // 탈퇴 시, 해당 사용자의 리프레시 토큰도 함께 삭제
        refreshTokenService.deleteByMemberId(memberId);

        // memberId(PK) 조회를 위해 회원 정보 가져오기
        MemberInfo memberInfo = readMemberByMemberId(memberId);

        // 회원 탈퇴 진행 (memberId 기반)
        memberMapper.deleteMember(memberInfo.getMemberId());

        // 누구에 의한 삭제인지 판한해 메세지 타입 반환
        if(isAdmin && !currentUserId.equals(memberId))    {
            //관리자가 본인이 아닌 남을 지웠을 때,
            return MessageType.ADMIN_KICK_SUCCESS;
        }else {
            // 본인이 삭제를 진행한 경우,
            return MessageType.DELETE_MEMBER_SUCCESS;
        }
    }

    /**
     * 이메일로 인증번호를 발송하고 DB에 저장합니다.
     */
    @Override
    @Transactional
    public void sendVerificationCode(String email) {

        // 이미 가입된 회원 여부 체크
        if(memberMapper.existsByUserEmail(email)){
            throw new ExistMemberException();
        }

        // 이미 인증 완료된 이메일 여부 체크
        int isVerified = emailVerificationMapper.checkAlreadyVerified(email);
        if(isVerified > 0 ){
            throw new ExistMailException();
        }

        // 1. 6자리 난수 생성
        String code = String.format("%06d", new Random().nextInt(1000000));
        
        // 2. DB 저장 (만료시간 5분)
        EmailVerification verification = EmailVerification.builder()
                .emailCheck(email)
                .verificationCode(code)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .build();
        emailVerificationMapper.upsertVerification(verification);

        // 3. 메일 발송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("[SQLD 게시판] 회원가입 이메일 인증 번호");
        message.setText("인증 번호는 [" + code + "] 입니다. 5분 이내에 입력해 주세요.");
        mailSender.send(message);
        
        log.info("Verification code sent to: {}", email);
    }

    /**
     * 사용자가 입력한 인증번호를 검증합니다.
     */
    @Override
    @Transactional
    public boolean verifyCode(String email, String code) {
        Optional<EmailVerification> found = emailVerificationMapper.findByEmail(email);
        
        if (found.isPresent()) {
            EmailVerification v = found.get();
            // 코드 일치 및 만료 시간 확인
            if (v.getVerificationCode().equals(code) && v.getExpiredAt().isAfter(LocalDateTime.now())) {
                emailVerificationMapper.updateIsVerified(email, "Y");
                return true;
            }
        }
        return false;
    }

    /**
     * 비밀번호 변경을 위한 인증번호를 발송합니다.
     * 아이디와 이메일이 일치하는지 먼저 확인합니다.
     */
    @Override
    @Transactional
    public void sendPassChangeCode(String userId, String email) {
        // 1. 아이디와 이메일 일치 확인
        if (!memberMapper.existsByUserIdAndEmail(userId, email)) {
            throw new RuntimeException("입력하신 아이디와 이메일 정보가 일치하지 않습니다.");
        }

        // 2. 인증번호 생성 및 DB 저장 (회원가입 로직 재사용 가능)
        String code = String.format("%06d", new Random().nextInt(1000000));
        EmailVerification verification = EmailVerification.builder()
                .emailCheck(email)
                .verificationCode(code)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .build();
        emailVerificationMapper.upsertVerification(verification);

        // 3. 메일 발송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("[SQLD 게시판] 비밀번호 변경 인증 번호");
        message.setText("비밀번호 변경을 위한 인증 번호는 [" + code + "] 입니다. 5분 이내에 입력해 주세요.");
        mailSender.send(message);

        log.info("Password change verification code sent to: {} for user: {}", email, userId);
    }

    /**
     * 비밀번호 변경을 위한 인증번호를 검증합니다.
     */
    @Override
    @Transactional
    public boolean verifyPassChangeCode(String userId, String email, String code) {
        // 아이디-이메일 일치 여부 다시 확인 (보안 강화)
        if (!memberMapper.existsByUserIdAndEmail(userId, email)) {
            return false;
        }
        return verifyCode(email, code);
    }

    /**
     * 비밀번호를 최종적으로 변경합니다.
     */
    @Override
    @Transactional
    public boolean updatePassword(String userId, String email, String newPassword) {
        // 1. 해당 이메일이 인증되었는지 확인
        EmailVerification v = emailVerificationMapper.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("인증 정보를 찾을 수 없습니다."));

        if (!"Y".equals(v.getIsVerified())) {
            throw new RuntimeException("인증이 완료되지 않았습니다.");
        }

        // 2. 아이디-이메일 일치 확인 및 회원 정보 조회
        MemberInfo memberInfo = memberMapper.readMemberByUsersId(userId)
                .orElseThrow(() -> new MemberNotFoundException());
        if (!memberInfo.getUserEmail().equals(email)) {
            throw new RuntimeException("아이디와 이메일 정보가 일치하지 않습니다.");
        }

        // 3. 비밀번호 업데이트 (memberId 기반)
        int result = memberMapper.updatePassword(memberInfo.getMemberId(), passwordEncoder.encode(newPassword));
        
        // 4. 보안을 위해 인증 정보 초기화
        emailVerificationMapper.updateIsVerified(email, "N");
        
        return result == 1;
    }

    /**
     * 사용자의 프로필 이미지를 업데이트합니다.
     */
    @Override
    @Transactional
    public boolean updateProfileImage(String memberId, String profileImage) {
        return memberMapper.updateProfileImage(memberId, profileImage) == 1;
    }

    /**
     * 사용자 ID 중복 여부를 확인합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkUserIdDuplicate(String userId) {
        return memberMapper.existsByUserId(userId);
    }

    /**
     * 사용자 이름(닉네임) 중복 여부를 확인합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkUserNameDuplicate(String userName) {
        return memberMapper.existsByUserName(userName);
    }

    /**
     * 사용자의 로그인을 처리하고, 인증 성공 시 회원 정보와 토큰을 포함한 결과를 반환합니다.
     *
     * @param req 로그인 요청 정보 (사용자 ID, 비밀번호)
     * @return {@link SignInResult} 객체 (회원정보, 액세스 토큰, 리프레시 토큰)
     * @throws SignInFailureException 비밀번호가 일치하지 않을 경우
     */
    @Override
    @Transactional
    public SignInResult signIn(SignInMemberReq req) {

        // 회원 id 조회
        MemberInfo foundMember = readMemberByUsersId(req.getUserId());
        //1. 비밀번호 검증
        if (!passwordEncoder.matches(req.getUserPass(), foundMember.getUserPass())) {
            throw new SignInFailureException();
        }

        // 보안 강화 (중복 로그인 막을 때 실행)
        //refreshTokenService.deleteByUserId(req.getUserId());

        //2. [추가] 최종 접속 시간 기록(AdminService 호출)
        // 최근 접속시간 저장 (memberId 기반)
        memberMapper.updateLastLogin(foundMember.getMemberId());

        // JWT의 subject에는 고유 식별자인 memberId를 사용
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", foundMember.getUserRole());
        claims.put("memberId", foundMember.getMemberId());

        String accessToken = jwtHandler.createToken(jwtSecret, claims, accessTokenExpiration, foundMember.getMemberId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(foundMember.getMemberId());

        return new SignInResult(foundMember, accessToken, refreshToken);
    }
    
    /**
     * 제공된 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급합니다.
     *
     * @param refreshTokenString 리프레시 토큰 문자열
     * @return 새로운 액세스 토큰 및 사용자 정보를 포함하는 {@link Optional}. 리프레시 토큰이 유효하지 않으면 빈 Optional.
     */
    @Override
    @Transactional
    public Optional<SignInResponseDto> refreshAccessToken(String refreshTokenString) {
        return refreshTokenService.findByToken(refreshTokenString)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getMemberId)
                .map(memberId -> {
                    MemberInfo memberInfo = readMemberByMemberId(memberId);
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("role", memberInfo.getUserRole());
                    claims.put("memberId", memberInfo.getMemberId());
                    
                    String newAccessToken = jwtHandler.createToken(jwtSecret, claims, accessTokenExpiration, memberInfo.getMemberId());

                    // 세션 복구를 위해 사용자 정보를 함께 반환
                    return SignInResponseDto.builder()
                            .memberId(memberInfo.getMemberId())
                            .userId(memberInfo.getUserId())
                            .userName(memberInfo.getUserName())
                            .userRole(memberInfo.getUserRole())
                            .profileImage(memberInfo.getProfileImage())
                            .accessToken(newAccessToken)
                            .build();
                });
    }


    /**
     * 사용자의 로그아웃을 처리합니다.
     * 데이터베이스에서 해당 사용자의 리프레시 토큰을 삭제합니다.
     *
     * @param memberId 로그아웃할 사용자의 ID
     */
    @Override
    @Transactional
    public void logout(String memberId) {
        refreshTokenService.deleteByMemberId(memberId);
        log.info("User logged out and refresh token deleted for userId: {}", memberId);
    }


    /**
     * 새로운 사용자를 등록(회원가입)하고, 생성된 ID를 부여합니다.
     * ID는 'MEMBER_YYYYMMDD_시퀀스' 형식으로 생성됩니다.
     *
     * @param req 회원가입 요청 정보를 담은 DTO
     * @return 회원가입 성공 시 true, 실패 시 false
     */
    @Override
    @Transactional
    public boolean signUpMember(SignUpMemberReq req) {
        try {
            // 0-1. 이메일 인증 여부 최종 확인
            EmailVerification v = emailVerificationMapper.findByEmail(req.getUserEmail())
                    .orElseThrow(() -> new RuntimeException("인증 정보를 찾을 수 없습니다."));

            if (!"Y".equals(v.getIsVerified())) {
                throw new RuntimeException("이메일 인증이 완료되지 않았습니다.");
            }

            // 0-2. 아이디 및 이름 중복 최종 확인
            if (memberMapper.existsByUserId(req.getUserId())) {
                throw new RuntimeException("이미 사용 중인 아이디입니다.");
            }
            if (memberMapper.existsByUserName(req.getUserName())) {
                throw new RuntimeException("이미 사용 중인 이름입니다.");
            }

            // 1. MySQL member 테이블을 통해 다음 시퀀스 번호 생성 및 가져오기
            Map<String, Object> params = new HashMap<>();
            params.put("name", req.getMemberId());
            memberMapper.insertMemberSequence(params);

            Long nextSeq = ((Number) params.get("id")).longValue();

            // 2. 날짜 형식 지정 및 ID 생성
            String currentDate = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            String memberId = String.format("MEMBER_%s_%03d", currentDate, nextSeq);

            // 3. 사용자 역할(Role) 변환
            String role = Role.USER.name();
            if ("2".equals(req.getUserRole())) {
                role = Role.ADMIN.name();
            }


            // 4. MemberInfo 모델 객체 생성
            MemberInfo memberInfo = MemberInfo.builder()
                    .memberId(memberId)
                    .userId(req.getUserId())
                    .userPass(passwordEncoder.encode(req.getUserPass()))
                    .userName(req.getUserName())
                    .userEmail(req.getUserEmail())
                    .emailVerified(req.getEmailVerified())
                    .userRole(role)
                    .userStatus("Y")
                    .build();

            // 5. 데이터베이스에 저장
            int result = memberMapper.signUpMember(memberInfo);
            if(result == 1){
                emailVerificationMapper.deleteVerification(req.getUserEmail());
            }
            return result == 1;
        } catch (Exception e) {
            log.error("회원가입 실패", e);
            throw new RuntimeException("회원가입 처리 중 오류가 발생했습니다.", e);
        }
    }

    /**********************/
    /*** Private Method ***/
    /**********************/

    /**
     * 사용자 ID를 기반으로 회원 정보를 조회합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @return 조회된 회원 정보
     * @throws MemberNotFoundException 해당 ID의 사용자를 찾을 수 없을 때 발생
     */
    private MemberInfo readMemberByUsersId(String userId) {
        return memberMapper.readMemberByUsersId(userId).orElseThrow(MemberNotFoundException::new);
    }


    private MemberInfo readMemberByMemberId(String memberId) {
        return memberMapper.readMemberByMemberId(memberId).orElseThrow(MemberNotFoundException::new);
    }
}
