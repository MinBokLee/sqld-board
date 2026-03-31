package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.model.member.EmailVerification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface EmailVerificationMapper {

    // 회원 가입 완료 후, 인증된 이메일 삭제
    void deleteVerification(@Param("email") String email);

    // 이미 인증이 진행된 메일인지 확인
    int checkAlreadyVerified(@Param("email") String email);

    // 인증 정보 저장 또는 갱신
    int upsertVerification(EmailVerification verification);

    // 이메일로 인증 정보 조회
    Optional<EmailVerification> findByEmail(@Param("email") String email);

    // 인증 완료 처리
    int updateIsVerified(@Param("email") String email, @Param("status") String status);
}
