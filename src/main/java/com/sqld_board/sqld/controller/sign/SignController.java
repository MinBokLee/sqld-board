package com.sqld_board.sqld.controller.sign;

import com.sqld_board.sqld.common.util.DateTimeUtils;
import com.sqld_board.sqld.dto.request.sign.*;
import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.member.MemberSimpleInfoRes;
import com.sqld_board.sqld.dto.response.sign.SignInResponseDto;
import com.sqld_board.sqld.dto.response.sign.SignInResult;
import com.sqld_board.sqld.exception.MessageType;
import com.sqld_board.sqld.exception.common.LoginRequiredException;
import com.sqld_board.sqld.exception.common.RefreshTokenMissingException;
import com.sqld_board.sqld.handler.ResponseHandler;
import com.sqld_board.sqld.service.sign.SignService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SignController {

    private final SignService signService;
    private final ResponseHandler responseHandler;

    @Value("${jwt.refresh-token.expiration-seconds}")
    private long refreshTokenExpiration;

    @Operation(summary = "로그인 후, 간단 회원정보 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/member/readMemberSimpleInfo")
    public Response readMemberSimpleInfo(@AuthenticationPrincipal User user) {
        if(user == null){
            throw new LoginRequiredException(); // 로그인이 필요한 서비스 입니다.
        }

        String memberId = user.getUsername();
        MemberSimpleInfoRes memberInfoData = signService.readMemberSimpleInfo(memberId);

        return Response.success(memberInfoData);
    }

    @Operation(summary = "회원 삭제(단건)")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/member/deleteMember/{memberId}")
    public Response deleteMember(@PathVariable String memberId, @AuthenticationPrincipal User user){

        // 로그인 체크
        if(user == null){
            throw new LoginRequiredException(); // 로그인이 필요한 서비스 입니다.
        }

        MessageType resultMsg = signService.deleteByMemberId(memberId);
        return responseHandler.getSuccessResponse(resultMsg);
    }

    @Operation(summary = "비밀번호 변경 인증코드 요청")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/common/pass-change/request")
    public Response requestPassChange(@RequestBody PassChangeRequest req) {
            signService.sendPassChangeCode(req.getUserId(), req.getEmail());

            return responseHandler.getSuccessResponse(MessageType.MAIL_SEND_SUCCESS); // 인증 코드 메일이 성공적으로 발송되었습니다. 확인해주세요.
    }

    @Operation(summary = "비밀번호 변경 인증코드 확인")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/common/pass-change/verify")
    public Response verifyPassChange(@RequestBody PassVerifyRequest req) {
        signService.verifyPassChangeCode(req.getUserId(), req.getEmail(), req.getCode());

        return responseHandler.getSuccessResponse(MessageType.VERIFICATION_MAIL_SUCCESS); // 이메일 인증이 완료 되었습니다.
    }

    @Operation(summary = "비밀번호 최종 수정")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/common/pass-change")
    public Response updatePassword(@RequestBody PassUpdateRequest req) {
        signService.updatePassword(req.getUserId(), req.getEmail(), req.getNewPassword());

        return responseHandler.getSuccessResponse(MessageType.CHANGE_PASSWORD_SUCCESS); //비밀번호가 변경되었습니다.
    }

    @Operation(summary = "프로필 이미지 업데이트")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/member/profile-image")
    public Response updateProfileImage(@RequestBody ProfileImageReq req, @AuthenticationPrincipal User user) {

        if(user==null){
            throw new LoginRequiredException();// 로그인이 필요한 서비스 입니다.
        }

        String memberId = user.getUsername();

        signService.updateProfileImage(memberId, req.getProfileImage());
        return responseHandler.getSuccessResponse(MessageType.IMAGE_UPDATE_SUCCESS); // 프로필 이미지가 성공적으로 수정 되었습니다.
    }

    @Operation(summary = "이메일 인증번호 발송")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/common/send-code")
    public Response sendCode(@RequestBody EmailVerificationReq req) {
        signService.sendVerificationCode(req.getEmail());

        return responseHandler.getSuccessResponse(MessageType.MAIL_SEND_SUCCESS);
    }

    @Operation(summary = "이메일 인증번호 확인")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/common/verify-code")
    public Response verifyCode(@RequestBody EmailVerificationReq req) {
        signService.verifyCode(req.getEmail(), req.getCode());

        return responseHandler.getSuccessResponse(MessageType.VERIFICATION_MAIL_SUCCESS);

    }

    @Operation(summary = "아이디 중복 확인")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/common/check-id")
    public Response checkUserId(@RequestParam String userId) {
        signService.checkUserIdDuplicate(userId);

        return responseHandler.getSuccessResponse(MessageType.AVAILABLE_USER_ID); // 사용 가능한 아이디입니다.
    }

    @Operation(summary = "이름 중복 확인")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/common/check-name")
    public Response checkUserName(@RequestParam String userName) {
        signService.checkUserNameDuplicate(userName);

        return responseHandler.getSuccessResponse(MessageType.AVAILABLE_USER_NAME); // 사용 가능한 이름(닉네임) 입니다.
    }

    @Operation(summary = "액세스 토큰 재발급")
    @PostMapping("/auth/token-refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refresh-token", required = false) String refreshToken) {

        // 1.  쿠키 존재 여부 확인 (없으면 예외 발생)
        if (refreshToken == null) {
            throw new RefreshTokenMissingException(); // 리프레시 토큰이 없습니다.
        }

        // 2. 서비스 호출( 실패 시, 서비스 내부에서 예외 발생
        SignInResponseDto responseDto = signService.refreshAccessToken(refreshToken);

        // 3. 성공 시 헤더 설정 및 데이터 반환
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + responseDto.getAccessToken());

        return ResponseEntity.ok().headers(headers).body(Response.success(responseDto));

    }


    @Operation(summary = "로그인")
    @PostMapping("/auth/signIn")
    public ResponseEntity<Response> signIn(@RequestBody SignInMemberReq req) {
        SignInResult result = signService.signIn(req);
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", result.getRefreshToken().getRToken())
                .httpOnly(true).secure(false).sameSite("Lax").path("/").maxAge(refreshTokenExpiration).build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + result.getAccessToken());
        headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

        SignInResponseDto responseDto = SignInResponseDto.builder()
                .memberId(result.getMemberInfo().getMemberId())
                .userId(result.getMemberInfo().getUserId())
                .userName(result.getMemberInfo().getUserName())
                .userRole(result.getMemberInfo().getUserRole())
                .profileImage(result.getMemberInfo().getProfileImage())
                .accessToken(result.getAccessToken())
                .lastLogAt(DateTimeUtils.format(result.getMemberInfo().getLastLogAt()))
                .build();

        return ResponseEntity.ok().headers(headers).body(Response.success(responseDto));
    }

    @Operation(summary = "회원 가입")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/auth/signUp")
    public Response signUpMember(@RequestBody SignUpMemberReq req) {

        signService.signUpMember(req);
        return responseHandler.getSuccessResponse(MessageType.SIGNUP_SUCCESS);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/auth/logout")
    public ResponseEntity<Response> logout(Authentication authentication) {
        if (authentication != null) {
            signService.logout(authentication.getName());
        }

        // 쿠키 삭제 설정
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                                                      .maxAge(0) // 쿠기 수명 0 즉시 만료
                                                      .path("/") // 전체 경로에서 쿠키 유효성 제거
                                                      .build();

        // body 메시지 생성
        Response body = responseHandler.getSuccessResponse(MessageType.LOGOUT_SUCCESS);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(body);
    }
}
