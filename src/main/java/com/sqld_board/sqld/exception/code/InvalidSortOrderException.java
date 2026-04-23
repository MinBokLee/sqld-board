package com.sqld_board.sqld.exception.code;

import lombok.Getter;

@Getter
public class InvalidSortOrderException extends RuntimeException{
    private final int maxAvailableOrder; // 프론트에 알려줄 최대 허용 순번

    public InvalidSortOrderException(int  maxOrder) {
        this.maxAvailableOrder = maxOrder;
    }

}
