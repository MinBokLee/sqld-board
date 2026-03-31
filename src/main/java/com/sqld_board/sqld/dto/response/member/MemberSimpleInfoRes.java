package com.sqld_board.sqld.dto.response.member;

import com.sqld_board.sqld.common.util.DateTimeUtils;
import com.sqld_board.sqld.model.member.MemberInfo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberSimpleInfoRes {

    // 사용자 고유 식별자
    private String memberId;

    // 사용자 아이디
    private String userId;

    // 사용자 명
    private String userName;

    //사용자 이메일
    private String userEmail;

    // 사용자 권한
    private String userRole;

    // 사용자 프로필
    private String profileImage;

    // 작성한 글의 수
    private int postCount;

    // 작성한 댓글의 수
    private int commentCount;

    // 최근 접속 시간
    private String lastLogAt;

    public MemberSimpleInfoRes(MemberInfo memberInfo) {
        this.memberId = memberInfo.getMemberId();
        this.userId = memberInfo.getUserId();
        this.userName = memberInfo.getUserName();
        this.userEmail = memberInfo.getUserEmail();
        this.userRole = memberInfo.getUserRole();
        this.profileImage = memberInfo.getProfileImage();
        this.postCount = memberInfo.getPostCount();
        this.commentCount = memberInfo.getCommentCount();
        this.lastLogAt = DateTimeUtils.format(memberInfo.getLastLogAt());
    }
}
