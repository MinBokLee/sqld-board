package com.sqld_board.sqld.dto.request.board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(name="ScrapDeleteRequest" ,description ="삭제할 스크랩 페이지 정보" )
@NoArgsConstructor
@Data
public class ScrapDeleteRequest {

    @Schema(description ="삭제할 스크랩 페이지 정보" , requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> scrapIds; // 삭제할 스크랩 페이지 정보
}

/**
 *      requiredMode = Schema.RequiredMode.REQUIRED
 *    * REQUIRED: 반드시 있어야 함 (필수).
 *    * NOT_REQUIRED: 없어도 됨 (선택).
 *    * AUTO: 코드의 다른 어노테이션(예: @NotNull)을 보고 자동으로 결정함.
 */
