package com.sqld_board.sqld.dto.response.sign;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 성공 시, 액세스 토큰과 리프레시 토큰을 담아 반환하는 DTO입니다.
 */
@Getter
@AllArgsConstructor
public class SignInResponseDto {
    /**
     * 사용자의 인증 및 인가에 사용되는 액세스 토큰입니다.
     */
    private String accessToken;
    /**
     * 액세스 토큰이 만료되었을 때, 새로운 액세스 토큰을 발급받기 위해 사용되는 리프레시 토큰입니다.
     */
    private String refreshToken;
}
