package com.sqld_board.sqld.dto.response.sign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 로그인 성공 시, 클라이언트에게 반환될 사용자 정보와 토큰을 담는 DTO입니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SignInResponseDto {
    /**
     * 사용자의 고유 식별자 (PK). 게시글 작성 시 등에 사용됩니다.
     */
    @Schema(description = "멤버 고유 ID (PK)")
    private String memberId;

    /**
     * 사용자의 로그인 아이디.
     */
    @Schema(description = "사용자 아이디")
    private String userId;

    /**
     * 사용자 이름.
     */
    @Schema(description = "사용자 이름")
    private String userName;

    /**
     * 사용자의 역할 (e.g., "USER", "ADMIN").
     */
    @Schema(description = "사용자 역할")
    private String userRole;

    /**
     * 회원 상태. Y=활성, N=비활성
     */
    @Schema(description = "회원 상태")
    private String userStatus;
    /**
     * 사용자의 프로필 이미지 URL입니다.
     */
    @Schema(description = "프로필 이미지 URL")
    private String profileImage;

    /**
     * 사용자가 작성한 총 게시글 수입니다.
     */
    @Schema(description = "작성 게시글 수")
    private int postCount;

    /**
     * 사용자가 작성한 총 댓글 수입니다.
     */
    @Schema(description = "작성 댓글 수")
    private int commentCount;

    /**
     * 사용자의 인증 및 인가에 사용되는 액세스 토큰입니다.
     */
    @Schema(description = "액세스 토큰")
    private String accessToken;

    /**
     * 사용자의 최종접속 로그시간입니다.
     */
    @Schema(description = "최종접속 로그시간")
    private String lastLogAt;
}
