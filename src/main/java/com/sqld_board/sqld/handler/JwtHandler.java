package com.sqld_board.sqld.handler;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * JWT(Json Web Token)를 생성하고 검증하는 역할을 담당하는 핸들러 클래스입니다.
 */
@Component
@Slf4j
public class JwtHandler {

    /**
     * 지정된 비밀 키, 클레임, 만료 시간을 기반으로 JWT를 생성합니다.
     *
     * @param encoderKey     JWT 서명에 사용할 비밀 키 (Base64 인코딩)
     * @param privateClaims  토큰에 포함될 비공개 클레임 맵
     * @param maxAgeSeconds 토큰의 유효 기간 (초 단위)
     * @param subject        토큰의 주체 (일반적으로 사용자 ID)
     * @return 생성된 JWT 문자열
     */
    public String createToken(String encoderKey, Map<String, Object> privateClaims, long maxAgeSeconds, String subject) {
        Instant now = Instant.now();
        Key key = Keys.hmacShaKeyFor(encoderKey.getBytes());

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(maxAgeSeconds)))
                .addClaims(privateClaims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT 토큰에서 클레임을 추출합니다.
     *
     * @param encoderKey JWT 서명 검증에 사용할 비밀 키
     * @param token      클레임을 추출할 JWT 토큰
     * @return 클레임 맵을 포함하는 {@link Optional}. 유효하지 않은 경우 빈 Optional.
     */
    public Optional<Claims> getClaims(String encoderKey, String token) {
        try {
            Key key = Keys.hmacShaKeyFor(encoderKey.getBytes());
            return Optional.of(Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody());
        } catch (Exception e) {
            log.error("Error parsing claims: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * JWT 토큰의 유효성을 검증합니다.
     *
     * @param encoderKey JWT 서명 검증에 사용할 비밀 키
     * @param token      검증할 JWT 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String encoderKey, String token) {
        try {
            Key key = Keys.hmacShaKeyFor(encoderKey.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}


