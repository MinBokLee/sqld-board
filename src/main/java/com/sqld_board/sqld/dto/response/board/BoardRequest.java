package com.sqld_board.sqld.dto.response.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardRequest {
    private Long boardId;
    private String title;
    private String content;
    private Long memberId;
    private String boardType;
    private Long fileSize;
    private int viewCount;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String tagName;
    private Long userId;
}

