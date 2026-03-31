package com.sqld_board.sqld.model.board;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardScrap {
    private long boardScrapId;

    private long boardId;

    private String memberId;

    private LocalDateTime createAt;
}
