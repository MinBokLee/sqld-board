package com.sqld_board.sqld.dto.request.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 댓글 수정 요청 데이터를 전달하는 DTO 클래스입니다.
 */
@Schema(name = "commentModifyRequest", description = "댓글 수정 요청 정보")
@NoArgsConstructor
@Data
public class CommentModifyRequest {
    @Schema(description = "댓글 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long commentId;

    @Schema(description = "수정할 댓글 내용", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;
}
