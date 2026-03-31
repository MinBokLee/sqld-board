package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.model.token.RefreshToken;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface RefreshTokenMapper {

    int save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);
    
    Optional<RefreshToken> findByMemberId(String memberId);

    int deleteByMemberId(String memberId);
}
