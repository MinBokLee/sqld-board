package com.sqld_board.sqld.dto.response.admin;

import com.sqld_board.sqld.common.util.DateTimeUtils;
import com.sqld_board.sqld.model.member.MemberInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class MemberResponse {
    private String memberId;
    private String userId;
    private String userName;
    private String userEmail;
    private String userRole;
    private String userStatus;
    private String lastLoginAt;

    public MemberResponse(MemberInfo memberInfo) {
        this.memberId = memberInfo.getMemberId();
        this.userId = memberInfo.getUserId();
        this.userName = memberInfo.getUserName();
        this.userEmail = memberInfo.getUserEmail();
        this.userRole = memberInfo.getUserRole();
        this.userStatus = memberInfo.getUserStatus();
        this.lastLoginAt = DateTimeUtils.format(memberInfo.getLastLogAt());
    }
}

