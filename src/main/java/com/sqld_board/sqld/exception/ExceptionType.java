package com.sqld_board.sqld.exception;

import lombok.Getter;

@Getter
public enum ExceptionType {
    EXCEPTION("exception.code", "exception.msg"),
    SIGN_IN_FAILURE_EXCEPTION("signInFailureException.code","signInFailureException.msg"),
    MEMBER_NOT_FOUND_EXCEPTION("memberNotFoundException.code","memberNotFoundException.msg");

    private final String code;
    private final String message;

    ExceptionType(String code, String message)  {
        this.code = code;
        this.message = message;
    }
}
