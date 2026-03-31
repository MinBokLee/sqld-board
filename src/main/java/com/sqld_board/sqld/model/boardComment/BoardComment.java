package com.sqld_board.sqld.model.boardComment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 게시글 댓글의 데이터 모델 클래스입니다.
 * 데이터베이스의 'BOARD_COMMENT' 테이블의 한 행(row)을 나타냅니다.
 */
@Getter
@Setter
public class BoardComment {
    //
    private Long commentId;
    private Long boardId;
    private String memberId;
    private String content;
    private LocalDateTime createAt;
}
