package com.sqld_board.sqld.model.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 게시판 첨부 파일 정보를 저장하는 모델 클래스입니다.
 * DB의 board_file 테이블과 매핑됩니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardFile {
    private Long fileId;      // 파일 고유 ID (PK)
    private Long boardId;     // 연관된 게시글 ID (FK)
    private String originName; // 업로드된 실제 파일명
    private String saveName;   // 서버에 저장된 고유 파일명 (UUID 조합)
    private String filePath;   // 서버 내 파일 저장 절대 경로
    private Long fileSize;     // 파일 크기 (bytes)
    private LocalDateTime createAt; // 파일 업로드 일시
}
