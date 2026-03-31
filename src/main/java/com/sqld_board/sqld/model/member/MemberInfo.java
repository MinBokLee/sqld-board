package com.sqld_board.sqld.model.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 회원(사용자)의 핵심 데이터 모델 클래스입니다.
 * 데이터베이스의 'MEMBER_INFO' 테이블의 한 행(row)을 나타내며,
 * 사용자의 ID, 자격 증명, 역할 등 모든 관련 정보를 포함합니다.
 */
@Schema(name = "MemberInfo", description = "회원 정보")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberInfo {

    // sequence
    private String memberId;

    // 사용자 ID
    private String userId;

    // 비밀번호
    private String userPass;

    // 사용자명
    private String userName;

    // 사용자 이메일
    private String userEmail;

    // 인증여부
    private String emailVerified;

    //사용자 타입 (ADMIN / USER)
    private String userRole;

    // 회원 상(Y = 활성화, N = 비활성화)
    private String userStatus;

    // 프로필 이미지 URL
    private String profileImage;

    //가입 일시
    private Date createAt;

    //수정 일시
    private Date updateAt;

    // 최종 접속 일시
    private LocalDateTime lastLogAt;

    // 작성한 글의 수 (가상)
    private int postCount;

    // 작성한 댓글의 수 (가상)
    private int commentCount;

}
