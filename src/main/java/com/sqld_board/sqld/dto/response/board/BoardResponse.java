package com.sqld_board.sqld.dto.response.board;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sqld_board.sqld.model.board.Board;
import com.sqld_board.sqld.model.board.BoardFile;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardResponse {
    private Long boardId;
    private String memberId;
    private String title;
    private String content;
    private String userId;
    private String userName;
    private String profileImage;
    private String boardCode;
    private String categoryId;
    private int viewCount;
    private int likeCount; // 추천수 추가
    private int commentCount; // 댓글수 추가
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String tagName;
    private List<String> tags;
    private boolean isLiked; // 현재 사용자의 추천 여부 추가
    private Long seqNumber; // 가상 번호 필드 추가
    private List<BoardFile> fileList; // 첨부 파일 목록 추가
    private Long scrapId; // [추가] 스크랩 고유 번호

    @JsonProperty("isScrapped")
    private boolean isScrapped; // [추기] 스크랩 여부 확인

    public BoardResponse(Board board) {
        this.boardId = board.getBoardId();
        this.memberId = board.getMemberId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.userId = board.getUserId();
        this.userName = board.getUserName();
        this.profileImage = board.getProfileImage();
        this.boardCode = board.getBoardCode();
        this.categoryId = board.getCategoryId();
        this.viewCount = board.getViewCount();
        this.likeCount = board.getLikeCount();
        this.commentCount = board.getCommentCount();
        this.createAt = board.getCreateAt();
        this.updateAt = board.getUpdateAt();
        this.tagName = board.getTagName();
        this.tags = getListTagName(board.getTagName());
        this.scrapId = board.getScrapId();

    }

    public BoardResponse(Board board, Long seqNumber){
        this(board);                // 위쪽 생성자를 호출하여 모든 필드를 채운다.
        this.seqNumber = seqNumber; // 가상번호 추가 셋팅
    }

    public List<String> getListTagName(String tagName){
        // 1. null 갑 체크 & empty() 확인
        if(tagName != null && !tagName.isEmpty()){
            return Arrays.stream(tagName.split(",")) // 콤마 기준으로 분리
                         .map(String:: trim)               // 공백 제거
                         .filter(tag -> !tag.isEmpty())    // 빈 문자열 제외
                         .collect(Collectors.toList());    // 리스트로 변환
        }
            // 데이터가 없으면 빈 리스트 반환
            return  new ArrayList<>();
    }
}
