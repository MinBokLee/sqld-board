package com.sqld_board.sqld.exception;

import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.exception.common.MemberNotFoundException;
import com.sqld_board.sqld.exception.common.SignInFailureException;
import com.sqld_board.sqld.handler.ResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.sqld_board.sqld.exception.ExceptionType.*;


@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    private final ResponseHandler responseHandler;


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Exception e){
        log.info("error", e);
        return responseHandler.getFailureResponse(EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(SignInFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response SignInFailureException(Exception e) {
        log.info("SignInFailureException", e);
        return responseHandler.getFailureResponse(SIGN_IN_FAILURE_EXCEPTION);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response MemberNotFoundException() {
        return responseHandler.getFailureResponse(MEMBER_NOT_FOUND_EXCEPTION);
    }

}


