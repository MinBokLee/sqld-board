package com.sqld_board.sqld.dto.request.sign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "프로필 이미지 업데이트 요청")
public class ProfileImageReq {
    @Schema(description = "사용자 고유 식별자(PK)", example = "MEMBER_20260311_001")
    private String memberId;
    
    @Schema(description = "프로필 이미지 URL", example = "/uploads/profile.png")
    private String profileImage;
}
