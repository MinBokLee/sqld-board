package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.model.commentManagement.CommentManagement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 댓글 관리(Comment) 관련 데이터베이스 작업을 위한 MyBatis 매퍼 인터페이스입니다.
 * resources/mapper/Comment.xml 파일과 매핑됩니다.
 */
@Mapper
@Repository
public interface CommentMapper {

    void deleteCommentsByBoardIds(@Param("boardIds") List<Long> boardIds);

    /**
     * 댓글 의 댓글이 있는 경우, '삭제된 댓글입니다' 로 문구 수정 DELETE_YN은 N유지
     * @param commentId
     */
    void maskCommentAsDeleted(@Param("commentId") Long commentId);

    /**
     * 대댓글(자식) 댓글이 있는지 확인
     * @param commentId
     * @return
     */
    int getChildCommentCount(@Param("commentId") Long commentId);

    /**
     * 새로운 댓글을 등록합니다.
     * @param commentManagement 저장할 댓글 객체
     * @return 영향을 받은 행의 수 (1)
     */
    int insertComment(CommentManagement commentManagement);

    /**
     * 특정 게시글의 댓글 목록을 조회합니다.
     * @param boardId 게시글 ID
     * @return 댓글 리스트
     */
    List<CommentManagement> getCommentList(@Param("boardId") Long boardId);

    /**
     * 댓글 내용을 수정합니다.
     * @param commentId 댓글 ID
     * @param content 수정할 내용
     * @return 영향을 받은 행의 수
     */
    int updateComment(@Param("commentId") Long commentId, @Param("content") String content);

    /**
     * 댓글을 삭제 처리(Soft Delete)합니다.
     * @param commentId 댓글 ID
     * @return 영향을 받은 행의 수
     */
    void deleteComment(@Param("commentId") Long commentId);

    /**
     * 댓글 정보를 조회합니다. (검증용)
     * @param commentId 댓글 ID
     * @return 댓글 객체
     */
    CommentManagement getComment(@Param("commentId") Long commentId);
}
