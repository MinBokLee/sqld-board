package com.sqld_board.sqld.dto.response.code;

import com.sqld_board.sqld.model.code.CommonCodeGroup;
import jakarta.annotation.Nullable;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommonCodeGroupResponse {

    private String groupCode;
    private String groupName;
    private String useYn;
    private int sortOrder;
    private LocalDateTime createAt;

    public static CommonCodeGroupResponse modelToDto(CommonCodeGroup group) {
        return CommonCodeGroupResponse.builder()
                .groupCode(group.getGroupCode())
                .groupName(group.getGroupName())
                .useYn(group.getUseYn())
                .sortOrder(group.getSortOrder())
                .createAt(group.getCreateAt())
                .build();
    }
}
