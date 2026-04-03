package com.sqld_board.sqld.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sqld_board.sqld.constants.MessageConstants;
import com.sqld_board.sqld.exception.ExceptionType;
import com.sqld_board.sqld.exception.MessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 모든 API 응답을 위한 표준 형식의 래퍼(Wrapper) 클래스입니다.
 * API 응답 구조를 일관되게 유지하며, 성공/실패 여부, 응답 코드, 메시지 및 실제 결과 데이터를 포함합니다.
 */
// JSON 직렬화 시, null인 필드는 포함하지 않음
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Response {
    private boolean success; // 요청 성공 유무
    private int code;        // 응답 코드 (예: 200: 성공, 그 외는 에러 코드)
    private String msg;
    private String error;
    private Result result;   // 응답 결과 객체 (성공 시, 데이터 실패 시, 에러 메시지 포함)


    /**
     * 데이터 (포함)
     * @param code
     * @param msg
     * @param data
     * @return
     * @param <T>
     */
    public static <T> Response success(int code, String msg, T data) {
        return new Response(true, code, msg, null, new Success<>(data));
    }

    /**
     * 테이터 (미포함)
     * @param code
     * @param msg
     * @return
     */
    public static Response success(int code, String msg){
        return new Response(true, code, msg,null,null);
    }
    /**
     * 데이터 없이 성공 응답을 생성합니다.
     * @param msg 성공 메시지와 코드를 담고 있는 {@link MessageConstants}
     * @return 데이터가 없는 성공 응답 객체
     */
    public static <T> Response success(MessageConstants msg) {
        return new Response(true, msg.getStatusCode(),msg.getMessage(), null,null);
    }


    /**
     * 데이터를 포함한 성공 응답을 생성합니다.
     * @param msg 성공 메시지와 코드를 담고 있는 {@link MessageConstants}
     * @param data 응답에 포함될 데이터
     * @return 데이터를 포함한 성공 응답 객체
     * @param <T> 데이터의 제네릭 타입
     */
    public static <T> Response success(MessageConstants msg, T data) {
        return new Response(true, msg.getStatusCode(), msg.getMessage(),null, new Success<>(data));
    }

    /**
     * 데이터만 포함한 성공 응답을 생성합니다. (기본 성공 코드: 200)
     * @param data 응답에 포함될 데이터
     * @return 데이터를 포함한 성공 응답 객체
     * @param <T> 데이터의 제네릭 타입
     */
    public static <T> Response success(T data) {
        return new Response(true, 200, "Success", null, new Success<>(data));
    }

    /**
     * {@link MessageConstants}를 사용한 실패 응답을 생성합니다.
     * @param msg 실패 메시지와 코드를 담고 있는 {@link MessageConstants}
     * @return 실패 응답 객체
     */
    public static Response failure(MessageConstants msg){
        return new Response(false, msg.getStatusCode(), msg.getMessage(), null,null);
    }

    /**
     * 사용자 정의 코드와 메시지를 사용한 실패 응답을 생성합니다.
     * @param code 사용자 정의 실패 코드
     * @param msg 사용자 정의 실패 메시지
     * @return 실패 응답 객체
     */
    public static Response failure(int code, String msg){
        return new Response(false, code, msg, null, null);
    }



}