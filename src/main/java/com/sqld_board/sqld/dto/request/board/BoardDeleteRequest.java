package com.sqld_board.sqld.dto.request.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(name = "BoardDeleteRequest", description = "게시글 일괄 삭제 요청")
public class BoardDeleteRequest {

    @Schema(description = "삭제할 게시글 ID 리스트", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> boardIds;
}
