package com.sqld_board.sqld.dto.response;

/**
 * API 응답의 결과(성공 또는 실패)를 나타내는 마커(marker) 인터페이스입니다.
 * {@link Success}와 {@link Failure} 클래스가 이 인터페이스를 구현하며,
 * {@link Response} 클래스에서 다형성을 통해 다양한 결과 타입을 포함하는 데 사용됩니다.
 */
public interface Result {
}
