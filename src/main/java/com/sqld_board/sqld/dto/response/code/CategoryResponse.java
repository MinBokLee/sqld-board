package com.sqld_board.sqld.dto.response.code;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private String categoryId;
    private String categoryName;
}
