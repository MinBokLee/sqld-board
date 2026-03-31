package com.sqld_board.sqld.dto.request.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 댓글 작성 요청 데이터를 전달하는 DTO 클래스입니다.
 */
@Schema(name = "commentRequest", description = "댓글 작성 요청 정보")
@NoArgsConstructor
@Data
public class CommentRequest {
    @Schema(description = "게시글 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long boardId;

    @Schema(description = "작성자 고유 식별자 (PK)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String memberId;

    @Schema(description = "댓글 내용", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "부모 댓글 아이디 (대댓글일 경우에만 전송)")
    private Long parentCommentId;
}
