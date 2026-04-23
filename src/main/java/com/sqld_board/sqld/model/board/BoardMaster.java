package com.sqld_board.sqld.model.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(name = "BoardMaster", description = "보드 마스터")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardMaster {
    private String boardCode;
    private String boardName;
    private String groupCode;
    private String fileYn;
    private String useYn;
    private String replyYn;
    private int sortOrder; // COMMON_CODE_GROUP - SORT_ORDER 추가
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
