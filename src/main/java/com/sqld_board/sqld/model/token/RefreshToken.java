package com.sqld_board.sqld.model.token;

import lombok.*;

import java.time.Instant;

/**
 * 리프레시 토큰의 데이터 모델 클래스입니다.
 * 데이터베이스의 'REFRESH_TOKEN' 테이블과 매핑됩니다.
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RefreshToken {

    // Primary Key
    private Integer rId;

    // Member ID (PK)
    private String memberId;

    // Refresh Token
    private String rToken;

    // Expiry Date
    private Instant expiryDate;

}
