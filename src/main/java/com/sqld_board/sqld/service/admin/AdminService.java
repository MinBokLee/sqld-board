package com.sqld_board.sqld.service.admin;

import com.sqld_board.sqld.dto.response.admin.MemberResponse;
import com.sqld_board.sqld.exception.MessageType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminService {

    void restore(@Param("boardId") Long boardId);

    void changeRoleAdmin(@Param("memberId") String memberId);

    /**
     * 회원 일괄 삭제 By Admin
     * @param userIds
     * @return
     */
    MessageType deleteMembersByAdmin(@Param("userIds") List<String> userIds);

    /**
     * 회원 리스트를 보여준다.
      * @return
     */
    List<MemberResponse> getMemberList();
}
