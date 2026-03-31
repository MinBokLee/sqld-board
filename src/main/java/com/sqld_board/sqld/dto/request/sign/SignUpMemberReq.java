package com.sqld_board.sqld.dto.request.sign;

import com.sqld_board.sqld.model.member.MemberInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청에 필요한 사용자 정보를 전달하는 DTO 클래스입니다.
 */
@Schema(name = "signUpMemberReq", description = "회원가입")
@NoArgsConstructor
@Data
public class SignUpMemberReq {

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

//    //가입 일시
//    private String createAt;
//
//    //수정 일시
//    private String updateAt;

}
