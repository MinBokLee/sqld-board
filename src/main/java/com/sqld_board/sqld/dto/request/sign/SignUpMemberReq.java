package com.sqld_board.sqld.dto.request.sign;

import com.sqld_board.sqld.model.member.MemberInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "signUpMemberReq", description = "회원가입")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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


    public static MemberInfo toModel(SignUpMemberReq req){

        String role = "";
        switch(req.getUserRole()) {
            case "1":
                role = "USER";
                break;
            case "2":
                role = "ADMIN";
                break;
            default:
                // 기본값으로 ADMIN 설정
                role = "USER";
        }

        return MemberInfo.builder()
                .userId(req.getUserId())
                .userEmail(req.getUserEmail())
                .userName(req.getUserName())
                .userPass(req.getUserPass())
                .userRole(role)
                .build();
    }

}
