package com.sqld_board.sqld.dto.request.sign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 이메일 인증 요청을 위한 DTO 클래스입니다.
 */
@Data
@NoArgsConstructor
public class EmailVerificationReq {
    @Schema(description = "이메일 주소", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "인증 코드 (검증 시에만 사용)")
    private String code;
}
