package com.sqld_board.sqld.service.board;

import com.sqld_board.sqld.dto.request.board.BoardRequest;
import com.sqld_board.sqld.dto.request.board.CommentRequest;
import com.sqld_board.sqld.dto.response.board.BoardResponse;
import com.sqld_board.sqld.model.board.BoardFile;
import com.sqld_board.sqld.model.board.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BoardService {

  /**
   * 내 스크랩 페이지 삭제
   * @param scrapId
   * @param memberId
   */
   void deleteMyScrapPage(@Param("scrapId") List<Long> scrapId, @Param("memberId") String memberId);

  /**
   * 내 스크랩 페이지 조회
   * @param memberId
   * @return
   */
   Map<String, Object> searchScrapMyPage(@Param("page")int page, @Param("size")int size
                                        ,@Param("keyword") String keyword, @Param("memberId") String memberId);

   /**
    *  스크랩 추가
     * @param boardId
    * @param memberId
    * @return
    */
   Boolean insertBoardScrap(@Param("boardId")Long boardId, @Param("memberId") String memberId);

   /**
    * 첨부파일 삭제
    * @param fileIds
    */
   void deleteFiles(@Param("fileIds") List<Long> fileIds);

   /**
    * 첨부파일 다운로드 시, 파일정보 조회
    * @param fileId
    * @return
    */
   BoardFile getBoardFileInfo(Long fileId);

   /**
    * 첨부 파일 다운로드
    * @param fileId
    * @return
    */
   Resource downloadFile(Long fileId);

   /**
    * 내가 작성한 글 리스트 조회
    * @param page
    * @param size
    * @param boardType
    * @param category
    * @param tagName
    * @param memberId
    * @return
    */
   Map<String, Object> getBoardMyList(int page, int size, String boardType
                                     ,String category, String tagName
                                     ,String keyword, String memberId);

   /**
    * 페이징된 게시판 목록 조회
    * @param page
    * @param size
    * @param boardType
    * @param category
    * @param tagName
    * @param memberId
    * @return
    */
   Map<String, Object> getBoardListWithPaging(int page, int size, String boardType, String category, String tagName, String memberId);

   /**
    * 전체 게시글 목록을 조회합니다.
    * @return
    */
   List<BoardResponse> getBoardList();

   /**
    * CKEditor 5 에디터 전용 이미지 업로드
    * @param file
    * @return
    * @throws IOException
    */
   String uploadImage(MultipartFile file)throws IOException;

    /** 게시판 단건조회
     *
     * @param boardId
     * @param memberId
     * @return
     */
     BoardResponse getSearchBoard(Long boardId, String memberId);

    /**
     * 파일 업로드
     * @param boardId
     * @param files
     */
   void uploadFiles(Long boardId, List<MultipartFile> files);

    /**
     * 게시글 작성하기
     * @param request
     * @return
     */
    Long writeBoard(BoardRequest request, String memberId);

    /**
     * 댓글 리스트 가져오기
     * @param boardId
     * @return
     */
    List<Comment> getCommentList(Long boardId);

    /**
     * 댓글 작성
     * @param request 댓글 정보
     * @param memberId 인증된 작성자 ID
     * @return 생성된 댓글 ID
     */
    Long writeComment(CommentRequest request, String memberId);

    /**
     * 댓글 수정
     * @param commentId
     * @param comment
     * @param memberId
     */
    void updateComment(Long commentId, String comment, String memberId);

    /**
     * 댓글 삭제
     * @param commentId
     * @param memberId
     * @Param parentCommentId
     */
    void deleteComment(Long commentId, String memberId);

    /**
     * 게시글 업데이트
     * @param boardId
     * @param boardReq
     * @param memberId
     */
    void updateBoard(Long boardId, BoardRequest boardReq, String memberId);


    /**
     * 게시글 조회수 증가
     * @param boardId
     * @return
     */
    void incrementViewCount(Long boardId);

    /**
     * 게시글 삭제
     * @param boardId
     * @param memberId
     */
    boolean deleteBoards(List<Long> boardId, String memberId);

    /**
     * 인기 게시글 조회
     * @return
     */
    List<BoardResponse> getPopularBoards();

    /**
     * 전문 검색(Full-Text Search)을 사용하여 게시글을 검색합니다.
     * @param keyword
     * @param boardType
     * @param page
     * @param size
     * @return
     */
    Map<String, Object> searchBoardContent(String keyword, String boardType, int page, int size);

    /**
     * 추천을 토글(추가 또는 취소)합니다.
     * @param boardId
     * @param memberId
     * @return
     */
    boolean toggleLike(Long boardId, String memberId);
}
