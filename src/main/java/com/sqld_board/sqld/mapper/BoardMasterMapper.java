package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.model.board.BoardMaster;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface BoardMasterMapper {

    Boolean existsByBoardNameExceptMe(@Param("boardName") String boardName,@Param("boardCode") String boardCode);

    /**
     * 5. updateBoardMaster 게시판 설정 수정
     * @param boardMaster
     */
    void updateBoardMaster(BoardMaster boardMaster);

    /**
     * 4. 특정 게시판의 설정(이름, 카테고리 그룹, 기능 여부) 전세 조회
     * @param boardCode
     * @return
     */
    Optional<BoardMaster> readBoardMasterDetail(@Param("boardCode") String boardCode);

    /**
     * 3. 특정 게시판의 설정(이름, 카테고리 그룹, 기능 여부) 전체 리스트 조회
     * @return
     */
    List<BoardMaster> getBoardConfigList();

    /**
     * 2. boardCode 중복 확인
     * @param boardCode
     */
    Boolean validateBoardCodeExist(String boardCode);

    /**
     * 1. 새로운 게시판 설정 등록
     * @param boardMaster
     */
    void addBoardMaster(BoardMaster boardMaster);

}
