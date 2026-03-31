package com.sqld_board.sqld.model.board;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 게시글 추천 정보를 저장하는 모델 클래스입니다.
 * 중복 추천 방지를 위해 사용됩니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardLike {
    private Long likeId;      // 추천 고유 ID (PK)
    private Long boardId;     // 추천된 게시글 ID (FK)
    private String memberId;  // 추천한 사용자 ID (FK)
    private LocalDateTime createAt; // 추천 일시
}
