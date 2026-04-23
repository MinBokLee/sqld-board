    package com.sqld_board.sqld.dto.request.board;

    import io.swagger.v3.oas.annotations.media.Schema;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.springframework.web.multipart.MultipartFile;

    import java.util.List;

    /**
     * 게시판 글 등록 및 수정을 위한 요청 데이터 전달 객체입니다.
     */
    @Schema(name = "boardRequest", description = "게시판 글 등록/수정 요청 정보")
    @NoArgsConstructor
    @Data
    public class BoardRequest {
        @Schema(description = "게시글 제목", requiredMode = Schema.RequiredMode.REQUIRED)
        private String title;

        @Schema(description = "게시글 본문 (CKEditor 등 에디터 내용 포함)", requiredMode = Schema.RequiredMode.REQUIRED)
        private String content;

        @Schema(description = "사용자 고유 식별자 (PK)")
        private String memberId;

        @Schema(description = "게시판 타입")
        private String boardCode;

        @Schema(description = "게시글 카테고리 (question, tip, faq, etc.)")
        private String categoryId;

        @Schema(description = "태그명")
        private String tagName;

        @Schema(description = "첨부 파일 리스트")
        private List<MultipartFile> files;

        @Schema(description = "삭제할 파일 리스트")
        private List<Long> deleteFileIds; // 삭제할 파일(PK(FILE_ID) 리스트

    }
