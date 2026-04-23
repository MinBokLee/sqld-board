package com.sqld_board.sqld.dto.response.code;

import com.sqld_board.sqld.model.code.CommonCodeDetail;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommonCodeDetailResponse {
    private String  groupCode;
    private String  codeId;
    private String  codeName;
    private int sortOrder;
    private String useYn;
    private LocalDateTime createAt;

   public static CommonCodeDetailResponse modelToDto(CommonCodeDetail codeDetail){
         return CommonCodeDetailResponse.builder()
                                        .groupCode(codeDetail.getGroupCode())
                                        .codeId(codeDetail.getCodeId())
                                        .codeName(codeDetail.getCodeName())
                                        .sortOrder(codeDetail.getSortOrder())
                                        .useYn(codeDetail.getUseYn())
                                        .createAt(codeDetail.getCreateAt())
                                        .build();
    }
}
