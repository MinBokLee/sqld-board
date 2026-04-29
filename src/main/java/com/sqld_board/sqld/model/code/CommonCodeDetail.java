package com.sqld_board.sqld.model.code;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(name = "CommonCodeDetail.xml" , description = "공통 코드 상세")
@Getter
@Setter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommonCodeDetail {
    private String  groupCode;
    private String  codeId;
    private String  codeName;
    private Integer sortOrder;
    private String useYn;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
