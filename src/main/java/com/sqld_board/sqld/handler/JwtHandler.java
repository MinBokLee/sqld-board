package com.sqld_board.sqld.handler;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * JWT(Json Web Token)를 생성하고 검증하는 역할을 담당하는 핸들러 클래스입니다.
 */
@Component
public class JwtHandler {

    private final String type = "Bearer";

    /**
     * 지정된 비밀 키, 클레임, 만료 시간을 기반으로 JWT를 생성합니다.
     *
     * @param encoderKey     JWT 서명에 사용할 비밀 키 (Base64 인코딩)
     * @param privateClaims  토큰에 포함될 비공개 클레임 맵
     * @param maxAgeSecondes 토큰의 유효 기간 (초 단위)
     * @return 생성된 JWT 문자열
     */
    public String createToken(String encoderKey, Map<String, Object> privateClaims, long maxAgeSecondes) {
        Instant now = Instant.now();
        Key key = Keys.hmacShaKeyFor(encoderKey.getBytes());

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(String.valueOf(privateClaims.get("id")))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(maxAgeSecondes)))
                .addClaims(privateClaims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}


