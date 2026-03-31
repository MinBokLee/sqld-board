package com.sqld_board.sqld.model.board;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Board {

    private Long boardId;
    private String title;
    private String content;
    private String memberId;        // 시스템 고유 ID (예: MEMBER_20260227_001)
    private String userId;          // 로그인 아이디 (예: user123)
    private String userName;        // 작성자 이름 (JOIN용)
    private String profileImage;    // 작성자 프로필 이미지 (JOIN용)
    private String boardType;
    private String category;        // 게시글 카테고리 (question, tip, faq, etc.)
    private int viewCount;
    private int likeCount;          // 추천수 추가
    private int commentCount;       // 댓글 수 추가
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String tagName;
    private Long scrapId; // [추가] BoardScrap 기본키

}
