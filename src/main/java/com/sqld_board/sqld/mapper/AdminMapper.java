package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.model.member.MemberInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface AdminMapper {



    /**
     * 삭제된 게시글 복구
     * @param boardId
     */
    void restoreBoardContent(@Param("boardId")Long boardId);

    /**
     * 게시글 완전 삭제(배치) 30일 기준
     * @return
     */
    int deleteOldSoftDeletedBoards();

    /**
     *
     * 운영자 Role -> 사용자 Role 변경
     * @param memberId
     */
    void changeRoleUser(@Param("memberId") String memberId);

    /**
     * 사용자 Role-> 운영자로 Role 변경
     * @param memberId
     */
    void changeRoleAdmin(@Param("memberId") String memberId);

    /**
     * 회원 일괄 삭제 By Admin
     * @param memberIds
     * @return
     */
    int deleteMembersBySuperAdmin(@Param("memberIds")List<String> memberIds);

    /**
     * userRole 조회
     * @param memberId
     * @return
     */
    MemberInfo getMemberRole(@Param("memberId") String memberId);

    /**
     * userRole 업데이트 후, 사용자 최신정보 조회
     * @param memberId
     * @return
     */
    MemberInfo getMemberDetail(@Param("memberId") String memberId);

    /**
     * 회원 리스트를 조회.
     * @return
     */
    List<MemberInfo> getMemberList();

    /**
     * USER_STATUS값을 N으로 변경 글 작성 불가
     * @param memberId
     */
    void writeNotUser(@Param("memberId") String memberId);


    /**
     * USER_STATUS값을 Y로 변경 글 작성 가능
     * @param memberId
     */
    void writeUser(@Param("memberId") String memberId);
}
