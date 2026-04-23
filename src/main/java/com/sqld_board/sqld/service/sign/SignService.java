package com.sqld_board.sqld.service.sign;

import com.sqld_board.sqld.dto.request.sign.SignInMemberReq;
import com.sqld_board.sqld.dto.request.sign.SignUpMemberReq;
import com.sqld_board.sqld.dto.response.member.MemberSimpleInfoRes;
import com.sqld_board.sqld.dto.response.sign.SignInResponseDto;
import com.sqld_board.sqld.dto.response.sign.SignInResult;
import com.sqld_board.sqld.exception.MessageType;

import java.util.Optional;

public interface SignService {

    MemberSimpleInfoRes readMemberSimpleInfo(String memberId);

    /**
     * 회원의 userId를 받아 회원 정보를 삭제한다.(단건)
     * @param userId
     */
    MessageType deleteByMemberId(String userId);

    void sendVerificationCode(String email);

    void verifyCode(String email, String code);

    void sendPassChangeCode(String userId, String email);

    void verifyPassChangeCode(String userId, String email, String code);

    void updatePassword(String userId, String email, String newPassword);

    void updateProfileImage(String memberId, String profileImage);

    void checkUserIdDuplicate(String userId);

    void checkUserNameDuplicate(String userName);

    SignInResult signIn(SignInMemberReq req);

    SignInResponseDto refreshAccessToken(String refreshTokenString);

    void logout(String userId);

    boolean signUpMember(SignUpMemberReq req);
}
