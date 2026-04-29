package com.sqld_board.sqld.dto.request.code;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "GroupCodeRequest" , description = "그룹코드 등록 요청 정보")
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Data
public class GroupCodeRequest {
    private String groupCode;
    private String groupName;
    private String useYn;
    private Integer sortOrder;
}
