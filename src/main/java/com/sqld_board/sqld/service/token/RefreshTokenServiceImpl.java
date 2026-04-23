package com.sqld_board.sqld.service.token;

import com.sqld_board.sqld.exception.common.TokenExpiredException;
import com.sqld_board.sqld.handler.JwtHandler;
import com.sqld_board.sqld.mapper.RefreshTokenMapper;
import com.sqld_board.sqld.model.token.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

        private final RefreshTokenMapper refreshTokenMapper;
        private final JwtHandler jwtHandler;

        @Value("${jwt.secret}")
        private String jwtSecret;

        @Value("${jwt.refresh-token.expiration-seconds}")
        private long refreshTokenExpiration;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(String memberId) {
        // 기존 리프레시 토큰 삭제 (memberId 기반)
        refreshTokenMapper.deleteByMemberId(memberId);

        RefreshToken refreshToken = RefreshToken.builder()
                .memberId(memberId)
                .rToken(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(refreshTokenExpiration))
                .build();

        refreshTokenMapper.save(refreshToken);
        return refreshToken;    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenMapper.findByToken(token);    }

    @Override
    public Optional<RefreshToken> findByMemberId(String memberId) {
        return refreshTokenMapper.findByMemberId(memberId);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenMapper.deleteByMemberId(token.getMemberId()); // 만료된 토큰 삭제.
            throw new TokenExpiredException(); // 만료된 토큰 입니다.
        }
        return token;
    }

        @Override
        @Transactional
        public int deleteByMemberId(String memberId) {
            return refreshTokenMapper.deleteByMemberId(memberId);
        }
    }
