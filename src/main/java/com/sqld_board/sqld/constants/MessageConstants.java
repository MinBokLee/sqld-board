package com.sqld_board.sqld.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * API 응답에 사용될 표준 메시지와 상태 코드를 정의한 열거형(Enum) 클래스입니다.
 * 애플리케이션 전체에서 일관된 응답 메시지를 관리하기 위해 사용됩니다.
 */
@AllArgsConstructor
@Getter
public enum MessageConstants {
    /**
     *     java Enum 타입은 일정 개수의 상수 값을 정의하고, 그 외의 값은 허용하지 않음.
     *     Java의 enum은 컴파일러가 내부적으로 클래스로 변환하는 구조이며,
     *     반드시 enum 상수들이 클래스의 다른 멤버(필드나 생성자 등)보다 먼저 선언되어야 한다.
     */


    //200 Successful
    OK(200, "처리 완료되었습니다."),
    CREATE_OK(201,"추가 완료되었습니다."),
    UPDATE_OK(201,"수정 완료되었습니다."),
    DELETE_OK(201,"삭제 완료되었습니다."),
    LIKE_OK(200, "추천이 완료되었습니다."),
    LIKE_CANCEL_OK(200, "추천이 취소되었습니다."),
    LOGOUT_OK(200, "성공적으로 로그아웃되었습니다."),

    //204 NO CONTENT
    CONTENT_NO(204, "조회된 정보가 없습니다."),

    //300 NO
    NO(300, "처리 실패되었습니다."),
    CREATE_FAIL(301, "추가 실패되었습니다."),
    UPDATE_FAIL(301, "수정 실패되었습니다."),
    DELETE_FAIL(301, "삭제 실패되었습니다."),

    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER(400, "파라미터 값을 확인해주세요."),

    //401 인증오류
    UNAUTHORIZED(401,"다시 로그인 해주세요"),

    //403 FORBIDDEN 권한
    FORBIDDEN_FAIL(403, "접근권한 없습니다."),

    //404 NOT_FOUND 잘못된 리소스 접근
    DISPLAY_NOT_FOUND(404, "화면을 찾을수 없습니다."),
    FAIR_NOT_FOUND(404, "존재하지 않는 정보입니다.."),

    //409 CONFLICT 중복된 리소스
    ALREADY_SAVED_CONTENT(409, "중복 저장된 내용입니다."),

    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버에러 발생되었습니다.");

    private final int statusCode;
    private final String message;
}
