package com.sqld_board.sqld.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * API 요청 처리 실패 시의 결과를 나타내는 클래스입니다.
 * 일반적으로 실패 메시지를 포함하며, {@link Result} 인터페이스를 구현합니다.
 */
@Getter
@AllArgsConstructor
public class Failure implements Result{
    private String msg;
}
