package com.sqld_board.sqld.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * API 요청 처리 성공 시의 결과를 나타내는 클래스입니다.
 * 제네릭을 사용하여 모든 타입의 데이터(data)를 담을 수 있으며, {@link Result} 인터페이스를 구현합니다.
 * @param <T> 응답 데이터의 타입
 */
@Getter
@AllArgsConstructor
public class Success<T> implements Result {
    private T data; //응답 데이터
}
