package com.sqld_board.sqld.model.code;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommonCodeGroup {
    private String groupCode;
    private String groupName;
    private String useYn;
    private Integer sortOrder;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
