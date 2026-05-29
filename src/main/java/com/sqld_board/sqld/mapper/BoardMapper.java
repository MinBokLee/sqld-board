package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.dto.response.board.BoardResponse;
import com.sqld_board.sqld.model.board.Board;
import com.sqld_board.sqld.model.board.BoardFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * 게시판(Board) 관련 데이터베이스 작업을 위한 MyBatis 매퍼 인터페이스입니다.
 * resources/mapper/Board.xml 파일과 매핑됩니다.
 */
@Mapper
@Repository
public interface BoardMapper {


    int updateBoardDeleteYNByAdmin(@Param("boardIds")List<Long> boardIds
                                  ,@Param("deleteYn") String deleteYn);

    /**
     * 게시판별 카테고리 유효성 검증
     * @param boardCode 게시판 코드
     * @param categoryId 카테고리 아이디
     * @return 유효 여부(true/false)
     */
    Boolean isValidateCategoryForBoard(@Param("boardCode") String boardCode
                                     , @Param("categoryId")String categoryId);

    /**
     * Redis에서 집계된 조회수를 DB에 일괄 합산한다.
     * @param boardId
     * @param increaseCount
     */
    void updateViewCountBulk(@Param("boardId") Long boardId, @Param("increaseCount") int increaseCount);

    /**
     * 게시글 유무 확인(조회수 증가전 Resis)
     * @param boardId
     * @return
     */
    int existsBoardContent(@Param("boardId") Long boardId);


    /**
     *  스크랩 페이지 취소
     * @param boardId
     * @param memberId
     */
    void deleteMyScrapSinglePage(@Param("boardId") Long boardId, @Param("memberId") String memberId);

    /**
     * 파일 소프트 삭제
     * @param boardIds
     * @return
     */
     void updateFilesDeleteYnByBoardIds(@Param("boardIds") List<Long> boardIds);

    /**
     * 게시글 삭제 시, 소프트 삭제 적용
     * @param boardIds
     * @param memberId
     * @return
     */
    int updateBoardDeleteYN(@Param("boardIds") List<Long> boardIds, @Param("memberId") String memberId );

    /**
     * 페이지 스크랩 여부 확인
     * @param boardId
     * @param memberId
     * @return
     */
    int checkBoardScrap(@Param("boardId") Long boardId, @Param("memberId") String memberId);

    /**
     * 내 스크랩 페이지 삭제
     * @param scrapIds
     * @param memberId
     */
    void deleteMyScrapPage(@Param("scrapIds") List<Long> scrapIds, @Param("memberId") String memberId);

    int getSearchScrapTotalCount(@Param("keyword") String keyword
                                ,@Param("memberId") String memberId);

    /**
     * 내 스크랩 페이지 조회
     * @param memberId
     * @return
     */
    List<Board> searchScrapMyPage(@Param("offset") int offset
                                 ,@Param("size") int size
                                 ,@Param("keyword") String keyword
                                 ,@Param("memberId") String memberId);

    /**
     * 내 스크랩  추가
     * @param boarId
     * @param memberId
     * @return
     */
    void insertBoardScrap(@Param("boardId")Long boarId, @Param("memberId") String memberId);

    void deleteBoardFiles(@Param("fileIds") List<Long> fileIds);

    List<BoardFile> getBoardFileInfoByIds(@Param("fileIds") List<Long> fileIds);

    BoardFile getBoardFileInfo(@Param("fileId") Long fileId);

    List<Board> getRecentFallbackBoards();

    List<Board> getPopularBoards();

    void anonymizeCommentsByAdmin(@Param("memberIds") List<String> memberIds);

    void anonymizePostsByAdmin(@Param("memberIds") List<String> memberIds);

    int anonymizeComments(@Param("memberId") String memberId);

    int anonymizePosts(@Param("memberId") String memberId);

    /**
     * 15. 특정 게시글에 첨부된 파일 리스트를 조회합니다.
     * @param boardId 게시글 ID
     * @return 첨부 파일 리스트
     */
    List<BoardFile> getBoardFileList(@Param("boardId") Long boardId);

    /**
     * 14. ID를 기준으로 특정 게시글 하나를 조회합니다.
     * @param boardId 조회할 게시글의 ID
     * @return 조회된 {@link BoardResponse} DTO. 없을 경우 null.
     */
    BoardResponse getSearchBoard(@Param("boardId") Long boardId, @Param("memberId") String memberId);

    /**
     * 13. 페이징 처리를 하여 게시글 목록을 조회합니다.
     * @param offset 시작 위치
     * @param size 가져올 개수
     * @param boardCode 게시판 타입
     * @param categoryId 카테고리 (필터링용)
     * @param memberId 사용자 고유번호 (필터링용)
     * @return 조회된 게시글 리스트
     */
    List<Board> getBoardListWithPaging(@Param("offset") int offset,
                                       @Param("size") int size,
                                       @Param("boardCode") String boardCode,
                                       @Param("categoryId") String categoryId,
                                       @Param("memberId") String memberId,
                                       @Param("keyword") String keyword,
                                       @Param("tagName")  String tagName);

    /**
     * 12. 전체 게시글의 총 개수를 조회합니다.
     * @param boardCode 게시판 타입
     * @param categoryId 카테고리 (필터링용)
     * @param memberId 사용자 고유번호 (필터링용)
     * @return 전체 게시글 수
     */
    int getBoardTotalCount(@Param("boardCode") String boardCode
                          ,@Param("categoryId") String categoryId
                          ,@Param("memberId") String memberId
                          ,@Param("keyword") String keyword
                          ,@Param("tagName") String tagName);

    /**
     * 11. 모든 게시글 목록을 데이터베이스에서 조회합니다.
     * @return
     */
    List<Board> getBoardList();

    /**
     * 10. 새로운 게시글을 등록합니다.
     * @param board 등록할 게시글 데이터
     * @return 영향을 받은 행의 수
     */
    @Options(useGeneratedKeys = true, keyProperty = "boardId")
    int insertBoard(Board board);

    /**
     * 9. 게시글에 첨부된 파일 정보를 저장합니다.
     * @param boardFile 저장할 파일 데이터
     * @return 영향을 받은 행의 수
     */
    int insertBoardFile(BoardFile boardFile);

    /**
     * 8. 기존 게시글 정보를 수정합니다.
     * @param board 수정할 게시글 데이터
     * @return 영향을 받은 행의 수
     */
    int updateBoard(Board board);

    /** 조회수 Redis 사용으로 수정됨 미사용.
     * 7. 특정 게시글의 조회수를 1 증가시킵니다.
     * @param boardId 조회수를 증가시킬 게시글의 ID
     * @return 영향을 받은 행의 수 (보통 1)
     */
//    int incrementViewCount(Long boardId);
    
    /**
     * 6. 추천 기록을 추가합니다.
     */
    int insertBoardLike(@Param("boardId") Long boardId, @Param("memberId") String memberId);

    /**
     * 5. 추천 기록을 삭제(취소)합니다.
     */
    int deleteBoardLike(@Param("boardId") Long boardId, @Param("memberId") String memberId);

    /**
     * 4. 특정 게시글의 추천수(LIKE_COUNT)를 업데이트합니다.
     * @param boardId
     * @param amount
     * @return
     */
    int updateLikeCount(@Param("boardId") Long boardId, @Param("amount") int amount);

    /**
     * 3. 특정 사용자가 해당 게시글에 추천을 눌렀는지 확인합니다.
     */
    int checkBoardLike(@Param("boardId") Long boardId, @Param("memberId") String memberId);

    /**
     * 2. 전문 검색(Full-Text Search)을 사용하여 게시글을 검색합니다.
     * @param keyword 검색어
     * @param boardCode 게시판 타입 (필터링용)
     * @param offset 시작 위치
     * @param size 가져올 개수
     * @return 검색된 게시글 리스트
     */
    List<Board> searchBoardContent(@Param("keyword") String keyword, @Param("boardCode") String boardCode, @Param("offset") int offset, @Param("size") int size);

    /**
     * 1. 검색된 게시글의 총 개수를 조회합니다.
     * @param keyword 검색어
     * @param boardCode 게시판 타입 (필터링용)
     * @return 검색 결과 총 개수
     */
    int getSearchBoardCount(@Param("keyword") String keyword, @Param("boardCode") String boardCode);        }