package com.sqld_board.sqld.controller.sign;

import com.sqld_board.sqld.constants.MessageConstants;
import com.sqld_board.sqld.dto.request.sign.SignInMemberReq;
import com.sqld_board.sqld.dto.request.sign.SignUpMemberReq;
import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.sign.SignInResponseDto;
import com.sqld_board.sqld.service.sign.SignService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common")
public class SignController {

    private final SignService signService;

    /**
     * 사용자 로그인을 처리하고, 성공 시 액세스 토큰과 리프레시 토큰을 반환합니다.
     *
     * @param req 로그인 요청 정보를 담은 DTO (사용자 ID, 비밀번호)
     * @return 성공 시, 토큰 정보를 담은 {@link Response} 객체
     */
    @Operation(summary = "로그인", description = "사용자 ID와 비밀번호로 로그인을 처리하고, 성공 시 JWT 액세스 토큰과 리프레시 토큰을 발급합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/signIn")
    public Response signIn(@RequestBody SignInMemberReq req) {
        SignInResponseDto signInResponseDto = signService.signIn(req);
        return Response.success(signInResponseDto);
    }

    /**
     * 새로운 사용자를 등록(회원가입)합니다.
     *
     * @param req 회원가입 요청 정보를 담은 DTO
     * @return 성공 시, 생성되었다는 메시지를 담은 {@link Response} 객체
     */
    @Operation(summary = "회원 가입", description = "새로운 사용자를 시스템에 등록합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signUp")
    public Response signUpMember(@RequestBody SignUpMemberReq req) {

        boolean isSaved = signService.signUpMember(req);

        try {
            if (isSaved) {
                return Response.success(MessageConstants.CREATE_OK);
            } else {
                return Response.failure(MessageConstants.CREATE_FAIL);
            }
        } catch (Exception e) {
            // 로깅 또는 예외 핸들링
            return Response.failure(MessageConstants.INTERNAL_SERVER_ERROR); // 500
        }
    }

}
