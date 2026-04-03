package com.sqld_board.sqld.service.admin;

import com.sqld_board.sqld.dto.response.admin.MemberResponse;
import com.sqld_board.sqld.exception.MessageType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminService {

    void restore(@Param("boardId") Long boardId);

    MemberResponse changeRoleByAdmin(@Param("currentAdminId") String currentAdminId
                          ,@Param("isSuperAdmin") boolean isSuperAdmin
                          ,@Param("memberId") String memberId);

    /**
     * 회원 일괄 삭제 By Admin
     * @param memberIds
     * @return
     */
    MessageType deleteMembersBySuperAdmin(@Param("memberIds") List<String> memberIds);

    /**
     * 회원 리스트를 보여준다.
      * @return
     */
    List<MemberResponse> getMemberList();
}
