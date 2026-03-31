package com.sqld_board.sqld.dto.request.sign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "회원 일괄 삭제 요청")
public class MemberBulkDeleteReq {

    @Schema(description = "삭제할 사용자 Id 리스트", example = "['user1', 'user2']")
    private List<String> userIds;

}
