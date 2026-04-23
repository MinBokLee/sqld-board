package com.sqld_board.sqld.dto.response.board;

import com.sqld_board.sqld.dto.response.code.CategoryResponse;
import com.sqld_board.sqld.model.board.BoardMaster;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardMasterResponse {
    private String boardCode;
    private String boardName;
    private String groupCode;
    private String fileYn;
    private String useYn;
    private String replyYn;
    private int sortOrder;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @Builder.Default // categories 가  없는 경우 빈 배열을 default 값으로 설정
    private List<CategoryResponse> categories = new ArrayList<>();

    public static BoardMasterResponse of(BoardMaster boardMaster, List<CategoryResponse> categories) {
        return BoardMasterResponse.builder()
                                  .boardCode(boardMaster.getBoardCode())
                                  .boardName(boardMaster.getBoardName())
                                  .groupCode(boardMaster.getGroupCode())
                                  .fileYn(boardMaster.getFileYn())
                                  .useYn(boardMaster.getUseYn())
                                  .replyYn(boardMaster.getReplyYn())
                                  .createAt(boardMaster.getCreateAt())
                                  .updateAt(boardMaster.getUpdateAt())
                                  .sortOrder(boardMaster.getSortOrder())
                                  .categories(categories)
                                  .build();
    }

}
