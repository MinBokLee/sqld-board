    package com.sqld_board.sqld.service.sign;
    
    import com.sqld_board.sqld.dto.request.sign.SignInMemberReq;
    import com.sqld_board.sqld.dto.request.sign.SignUpMemberReq;
    import com.sqld_board.sqld.dto.response.sign.SignInResponseDto;
    import com.sqld_board.sqld.exception.common.MemberNotFoundException;
    import com.sqld_board.sqld.exception.common.SignInFailureException;
    import com.sqld_board.sqld.handler.JwtHandler;
    import com.sqld_board.sqld.mapper.MemberMapper;
    import com.sqld_board.sqld.model.member.MemberInfo;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    
    import java.util.HashMap;
    import java.util.Map;
    
    @Service
    @Slf4j
    @RequiredArgsConstructor
    public class SignService {
    
        private final MemberMapper memberMapper;
        private final PasswordEncoder passwordEncoder;
        private final JwtHandler jwtHandler;
    
        @Value("${jwt.secret}")
        private String jwtSecret;
    
        private final long accessTokenExpiration = 60 * 30; // 30 minutes
        private final long refreshTokenExpiration = 60 * 60 * 24 * 7; // 1 week
    
        /**
         * 사용자의 로그인을 처리하고, 성공 시 액세스 토큰과 리프레시 토큰을 생성하여 반환합니다.
         *
         * @param req 로그인 요청 정보를 담은 DTO (사용자 ID, 비밀번호)
         * @return 로그인 성공 시, 액세스 토큰과 리프레시 토큰을 포함한 DTO
         * @throws MemberNotFoundException 사용자를 찾을 수 없을 때 발생
         * @throws SignInFailureException  비밀번호가 일치하지 않을 때 발생
         */
        public SignInResponseDto signIn(SignInMemberReq req) {
            MemberInfo foundMember = readMemberByUsersId(req.getUserId());
            if (!passwordEncoder.matches(req.getUserPass(), foundMember.getUserPass())) {
                throw new SignInFailureException();
            }
    
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", foundMember.getMemberId());
            claims.put("role", foundMember.getUserRole());
    
            String accessToken = jwtHandler.createToken(jwtSecret, claims, accessTokenExpiration);
            String refreshToken = jwtHandler.createToken(jwtSecret, claims, refreshTokenExpiration);
    
            return new SignInResponseDto(accessToken, refreshToken);
        }
    
        /**
         * 새로운 사용자를 등록(회원가입)합니다.
         *
         * @param req 회원가입 요청 정보를 담은 DTO
         * @return 회원가입 성공 시 true, 실패 시 false
         */
        public boolean signUpMember(SignUpMemberReq req) {
            try {
                // 비밀번호 암호화
                req.setUserPass(passwordEncoder.encode(req.getUserPass()));
                MemberInfo signData = SignUpMemberReq.toModel(req);
    
                int result = memberMapper.signUpMember(signData);
                return result == 1;
            } catch (Exception e) {
                log.error("회원가입 실패", e);
            }
            return false;
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
    }    

