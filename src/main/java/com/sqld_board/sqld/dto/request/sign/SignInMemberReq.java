package com.sqld_board.sqld.dto.request.sign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "SignInMemberReq", description = "로그인 정보")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignInMemberReq {

    private String userId;

    private String userPass;
}
