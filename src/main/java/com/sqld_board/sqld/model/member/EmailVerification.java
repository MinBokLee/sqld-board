package com.sqld_board.sqld.model.member;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 이메일 인증 정보를 저장하는 모델 클래스입니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerification {
    private Long verificationId;
    private String emailCheck;      // 인증 대상 이메일
    private String isVerified;      // 인증 여부 (Y/N)
    private String verificationCode; // 인증 번호
    private LocalDateTime expiredAt; // 만료 시간
}
