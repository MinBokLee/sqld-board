package com.sqld_board.sqld.handler;

import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.exception.ExceptionType;
import com.sqld_board.sqld.exception.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 표준화된 {@link Response} 객체, 특히 실패 응답을 생성하는 유틸리티 클래스입니다.
 * {@link MessageSource}를 사용하여 프로퍼티 파일(i18n/exception.properties)에서
 * 로케일에 맞는 에러 코드와 메시지를 조회하여 실패 응답을 생성합니다.
 */
@Component
@RequiredArgsConstructor
public class ResponseHandler {

    private final MessageSource messageSource;

    /**
     * 예외 타입을 기반으로 실패 응답을 생성합니다.
     * @param exceptionType 에러 정보를 담고 있는 {@link ExceptionType}
     * @return 생성된 실패 응답 객체
     */
    public Response getFailureResponse(ExceptionType exceptionType) {
        return Response.failure(getCode(exceptionType.getCode()), getMessage(exceptionType.getMessage()));
    }

    /**
     * 예외 타입과 추가 인자를 기반으로 실패 응답을 생성합니다.
     * 메시지에 파라미터를 포함해야 할 경우 사용됩니다.
     * @param exceptionType 에러 정보를 담고 있는 {@link ExceptionType}
     * @param args 메시지 포맷팅에 사용될 인자
     * @return 생성된 실패 응답 객체
     */

    /**
     * 메시지 전송 i18n적용
     * @param msgType
     * @return
     */
    public Response getSuccessResponse(MessageType msgType) {
        //1. properties에서 해당하는 문구를 가져옴.
        String message = messageSource.getMessage(msgType.getMsg(), null, LocaleContextHolder.getLocale());

        //2. properties에서 코드 가져오기
        String codeStr = messageSource.getMessage(msgType.getCode(), null, LocaleContextHolder.getLocale());
        int code = Integer.parseInt(codeStr);

        return Response.success(code, message);
    }

    public Response getFailureResponse(ExceptionType exceptionType, Object... args) {
        return Response.failure(getCode(exceptionType.getCode()), getMessage(exceptionType.getMessage(),args));
    }


    private Integer getCode(String key){
        return Integer.valueOf(messageSource.getMessage(key,null, LocaleContextHolder.getLocale()));
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key,null, LocaleContextHolder.getLocale());
    }

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage( key, args, LocaleContextHolder.getLocale());
    }
}
