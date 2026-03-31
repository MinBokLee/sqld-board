package com.sqld_board.sqld.dto.response.sign;

import com.sqld_board.sqld.model.member.MemberInfo;
import com.sqld_board.sqld.model.token.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInResult {
    private MemberInfo memberInfo;
    private String accessToken;
    private RefreshToken refreshToken;
}
