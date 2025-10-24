package com.sqld_board.sqld.controller.sign;

import com.sqld_board.sqld.constants.MessageConstants;
import com.sqld_board.sqld.dto.request.sign.SignInMemberReq;
import com.sqld_board.sqld.dto.request.sign.SignUpMemberReq;
import com.sqld_board.sqld.dto.response.Response;
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


    @Operation(summary = "로그인")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/signIn")
    public Response readMemberByUsersId(@RequestBody SignInMemberReq req) {

        boolean isTrue = signService.signIn(req);

        if (isTrue) {
            return Response.success(MessageConstants.OK);
        } else {
            return Response.failure(MessageConstants.NO);
        }

    }

    @Operation(summary = "회원 가입")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signUp")
    public Response signUpMember(@RequestBody SignUpMemberReq req)  {

        boolean isSaved = signService.signUpMember(req);

        try{
            if(isSaved) {
                return Response.success(MessageConstants.CREATE_OK);
            } else {
                return Response.failure(MessageConstants.CREATE_FAIL);
            }
        } catch(Exception e){
            // 로깅 또는 예외 핸들링
            return Response.failure(MessageConstants.INTERNAL_SERVER_ERROR); // 500
        }
    }



}
