package com.sqld_board.sqld.service.token;

import com.sqld_board.sqld.model.token.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
   RefreshToken createRefreshToken(String memberId);
   Optional<RefreshToken> findByToken(String token);
   Optional<RefreshToken> findByMemberId(String memberId);
   RefreshToken verifyExpiration(RefreshToken token);
   int deleteByMemberId(String memberId);

}
