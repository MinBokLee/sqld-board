package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.dto.request.sign.SignInMemberReq;
import com.sqld_board.sqld.dto.request.sign.SignUpMemberReq;
import com.sqld_board.sqld.model.memberInfo.MemberInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface MemberMapper {

    int signUpMember(SignUpMemberReq req);

    Optional<MemberInfo> readMemberByUsersId(@Param ("userId") String userId);


}
