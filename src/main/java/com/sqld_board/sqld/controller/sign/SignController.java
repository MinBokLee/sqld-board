package com.sqld_board.sqld.controller.sign;

import com.sqld_board.sqld.common.util.DateTimeUtils;
import com.sqld_board.sqld.constants.MessageConstants;
import com.sqld_board.sqld.dto.request.sign.*;
import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.sign.SignInResponseDto;
import com.sqld_board.sqld.dto.response.sign.SignInResult;
import com.sqld_board.sqld.exception.MessageType;
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
import org.springframework.security.core.userdetails.UserDetails;
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
    @GetMapping("/member/readMemberSimpleInfo")
    public ResponseEntity<Response> readMemberSimpleInfo(@AuthenticationPrincipal User user) {
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.failure(401,"인증 정보가 없습니다."));
        }
        return signService.readMemberSimpleInfo(user.getUsername())
                .map(info -> ResponseEntity.ok(Response.success(info)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "회원 삭제(단건)")
    @DeleteMapping("/member/deleteMember/{memberId}")
    public ResponseEntity<Response> deleteMember(@PathVariable String memberId){
        MessageType resultType = signService.deleteByMemberId(memberId);
        return ResponseEntity.ok(responseHandler.getSuccessResponse(resultType));
    }

    @Operation(summary = "비밀번호 변경 인증코드 요청")
    @PostMapping("/common/pass-change/request")
    public ResponseEntity<Response> requestPassChange(@RequestBody PassChangeRequest req) {
        try {
            signService.sendPassChangeCode(req.getUserId(), req.getEmail());
            return ResponseEntity.ok(Response.success("인증번호가 발송되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.failure(400, e.getMessage()));
        }
    }

    @Operation(summary = "비밀번호 변경 인증코드 확인")
    @PostMapping("/common/pass-change/verify")
    public ResponseEntity<Response> verifyPassChange(@RequestBody PassVerifyRequest req) {
        boolean isVerified = signService.verifyPassChangeCode(req.getUserId(), req.getEmail(), req.getCode());
        return isVerified ? ResponseEntity.ok(Response.success("이메일 인증이 완료되었습니다.")) 
                          : ResponseEntity.badRequest().body(Response.failure(400, "인증번호가 틀렸습니다."));
    }

    @Operation(summary = "비밀번호 최종 수정")
    @PatchMapping("/common/pass-change")
    public ResponseEntity<Response> updatePassword(@RequestBody PassUpdateRequest req) {
        try {
            boolean isUpdated = signService.updatePassword(req.getUserId(), req.getEmail(), req.getNewPassword());
            return isUpdated ? ResponseEntity.ok(Response.success("비밀번호가 변경되었습니다.")) 
                             : ResponseEntity.badRequest().body(Response.failure(400, "변경 실패"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Response.failure(400, e.getMessage()));
        }
    }

    @Operation(summary = "프로필 이미지 업데이트")
    @PatchMapping("/member/profile-image")
    public ResponseEntity<Response> updateProfileImage(@RequestBody ProfileImageReq req) {
        boolean isUpdated = signService.updateProfileImage(req.getMemberId(), req.getProfileImage());
        return isUpdated ? ResponseEntity.ok(Response.success("저장되었습니다.")) 
                         : ResponseEntity.badRequest().body(Response.failure(400, "저장 실패"));
    }

    @Operation(summary = "이메일 인증번호 발송")
    @PostMapping("/common/send-code")
    public ResponseEntity<Response> sendCode(@RequestBody EmailVerificationReq req) {
        signService.sendVerificationCode(req.getEmail());
        return ResponseEntity.ok(responseHandler.getSuccessResponse(MessageType.MAIL_SEND_SUCCESS));
    }

    @Operation(summary = "이메일 인증번호 확인")
    @PostMapping("/common/verify-code")
    public ResponseEntity<Response> verifyCode(@RequestBody EmailVerificationReq req) {
        boolean isVerified = signService.verifyCode(req.getEmail(), req.getCode());
        return isVerified ? ResponseEntity.ok(Response.success("이메일 인증이 완료되었습니다.")) 
                          : ResponseEntity.badRequest().body(Response.failure(400, "인증번호가 틀렸습니다."));
    }

    @Operation(summary = "아이디 중복 확인")
    @GetMapping("/common/check-id")
    public ResponseEntity<Response> checkUserId(@RequestParam String userId) {
        return signService.checkUserIdDuplicate(userId) ? ResponseEntity.ok(Response.failure(409, "이미 사용 중인 아이디입니다.")) 
                                                        : ResponseEntity.ok(Response.success("사용 가능한 아이디입니다."));
    }

    @Operation(summary = "이름 중복 확인")
    @GetMapping("/common/check-name")
    public ResponseEntity<Response> checkUserName(@RequestParam String userName) {
        return signService.checkUserNameDuplicate(userName) ? ResponseEntity.ok(Response.failure(409, "이미 사용 중인 이름입니다.")) 
                                                            : ResponseEntity.ok(Response.success("사용 가능한 이름입니다."));
    }

    @Operation(summary = "액세스 토큰 재발급")
    @PostMapping("/auth/token-refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refresh-token", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.failure(401, "Refresh token is missing"));
        }
        return signService.refreshAccessToken(refreshToken)
                .map(responseDto -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + responseDto.getAccessToken());
                    return ResponseEntity.ok().headers(headers).body(Response.success(responseDto));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.failure(401, "Invalid or expired refresh token")));
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
    @PostMapping("/auth/signUp")
    public ResponseEntity<Response> signUpMember(@RequestBody SignUpMemberReq req) {
        boolean isSaved = signService.signUpMember(req);
        return isSaved ? ResponseEntity.status(HttpStatus.CREATED).body(Response.success(MessageConstants.CREATE_OK)) 
                       : ResponseEntity.badRequest().body(Response.failure(MessageConstants.CREATE_FAIL));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/auth/logout")
    public ResponseEntity<Response> logout(Authentication authentication) {
        if (authentication != null) {
            signService.logout(authentication.getName());
        }
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "").maxAge(0).path("/").build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(Response.success(MessageConstants.LOGOUT_OK));
    }
}
