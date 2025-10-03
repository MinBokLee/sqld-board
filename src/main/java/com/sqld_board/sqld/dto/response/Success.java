package com.sqld_board.sqld.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

/*
단일 데이터를 위한 응답 개체
 */
@Getter
@AllArgsConstructor
public class Success<T> implements Result {
    private T data; //응답 데이터
}
