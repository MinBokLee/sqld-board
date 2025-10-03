package com.sqld_board.sqld.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private Result result;   // 응답 결과 객체 (성공 시, 데이터 실패 시, 에러 메시지 포함)

    /**
     * 성공 응답 (데이터 없음)
     * 예: {"success": true, "code": 0}
     */
    public static Response success() {
        return new Response(true,0, null);
    }

    /**
     * 성공 응답 (데이터 포함)
     * 제네릭 T를 사용해 다양한 데이터 타입 지원
     * 예: {"success": true, "code": 0, "result": { "data": ... }}
     */
    public static <T> Response success(int code, T data) {
        return new Response(true, code, new Success<>(data));
    }

    /**
     * 실패 응답
     * 예: {"success": false, "code": 400, "result": { "message": "에러 메시지" }}
     */
    public static Response Failure(int code, String msg){
        return new Response(false, code, new Failure(msg));
    }

}
