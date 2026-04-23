package com.sqld_board.sqld.dto.request.code;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="DetailGroupCodeRequest", description = "그룹 상세 코드 의 내용을 전달")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupCodeDetailRequest {
    private String  groupCode;
    private String  codeId;
    private String  codeName;

    @Min(value = 1, message = "정렬 순서는 1 이상의 숫자여야 한다. ")
    private int sortOrder;
    private String useYn;

}
