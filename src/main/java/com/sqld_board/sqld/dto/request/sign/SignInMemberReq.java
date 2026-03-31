package com.sqld_board.sqld.dto.request.sign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 로그인 요청에 필요한 데이터를 전달하는 DTO 클래스입니다.
 * 사용자 ID와 비밀번호를 포함합니다.
 */
@Schema(name = "SignInMemberReq", description = "로그인 정보")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignInMemberReq {

    private String userId;

    private String userPass;
}
