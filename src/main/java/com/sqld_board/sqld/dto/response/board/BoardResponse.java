package com.sqld_board.sqld.dto.response.board;

import com.sqld_board.sqld.model.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponse {
    private Long boardId;
    private String title;
    private String content;
    private String memberId;
    private String boardType;
    private int viewCount;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public BoardResponse(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.memberId = board.getMemberId();
        this.boardType = board.getBoardType();
        this.viewCount = board.getViewCount();
        this.createAt = board.getCreateAt();
        this.updateAt = board.getUpdateAt();
    }
}
