package com.sqld_board.sqld.dto.request.board;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardMasterRequest {
    private String boardCode;
    private String boardName;
    private String groupCode;
    private String fileYn;
    private String useYn;
    private String replyYn;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
