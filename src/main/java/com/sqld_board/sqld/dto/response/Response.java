package com.sqld_board.sqld.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sqld_board.sqld.constants.MessageConstants;
import com.sqld_board.sqld.exception.ExceptionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
     * 성공 응답 (데이터 없음) "MessageConstants 를 사용"
     * 예: {"success": true, "code": 0}
     */
    public static <T> Response success(MessageConstants msg) {
        return new Response(true, msg.getStatusCode(),msg.getMessage(), null,null);
    }


    /**
     * 성공 응답 (데이터 포함) "MessageConstants 를 사용"
     * 제네릭 T를 사용해 다양한 데이터 타입 지원
     * 예: {"success": true, "code": 0, "result": { "data": ... }}
     */
    public static <T> Response success(MessageConstants msg, T data) {
        return new Response(true, msg.getStatusCode(), msg.getMessage(),null, new Success<>(data));
    }

    /**
     * 실패 응답 "MessageConstants 를 사용"
     * 예: {"success": false, "code": 400, "result": { "message": "에러 메시지" }}
     */
    public static Response failure(MessageConstants msg){
        return new Response(false, msg.getStatusCode(), msg.getMessage(), null,null);
    }

    public static Response failure(int code, String msg){
        return new Response(false, code, msg, null, null);
    }

}
