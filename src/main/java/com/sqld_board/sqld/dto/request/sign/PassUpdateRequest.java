package com.sqld_board.sqld.dto.request.sign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PassUpdateRequest {
    @Schema(description = "사용자 아이디", example = "user123")
    private String userId;
    @Schema(description = "이메일", example = "test@example.com")
    private String email;
    @Schema(description = "새 비밀번호", example = "newPassword123!")
    private String newPassword;
}
