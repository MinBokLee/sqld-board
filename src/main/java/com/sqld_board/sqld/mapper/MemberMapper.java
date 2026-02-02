package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.model.member.MemberInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface MemberMapper {

    int signUpMember(MemberInfo req);

    Optional<MemberInfo> readMemberByUsersId(@Param ("userId") String userId);


}
