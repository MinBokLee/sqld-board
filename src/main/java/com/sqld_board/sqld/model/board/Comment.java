package com.sqld_board.sqld.model.board;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 댓글 정보를 저장하는 모델 클래스입니다.
 * comment_management 테이블과 매핑됩니다.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment {

    private Long commentId;        // 댓글 아이디 (PK)
    private Long boardId;          // 게시글 아이디 (FK)
    private String memberId;       // 작성자 고유 아이디 (MEMBER_INFO의 PK)
    private String userName;       // 작성자 이름 (조회용)
    private String userId;         // 작성자 아이디 (조회용)
    private String content;        // 댓글 내용
    private Long parentCommentId;  // 부모 댓글 아이디 (대댓글용)
    private int likeCount;         // 좋아요 수
    private LocalDateTime createAt; // 작성일시
    private LocalDateTime updateAt; // 수정일시
    private String deleteYn;       // 삭제여부 (Y/N)
}
