package com.sqld_board.sqld.service.board;

import com.sqld_board.sqld.dto.request.board.BoardMasterRequest;
import com.sqld_board.sqld.dto.response.board.BoardMasterResponse;

import java.util.List;

public interface BoardMasterService {

    /**
     * updateBoardMaster 게시판 설정 수정
     * @param boardCode
     */
    void updateBoardMaster(String boardCode, BoardMasterRequest request);

    /**
     * 특정 게시판의 설정(이름, 카테고리 그룹, 기능 여부) 전체 리스트 조회
     * @param boardCode
     * @return
     */
    BoardMasterResponse readBoardMasterDetail(String boardCode);

    /**
     * 특정 게시판의 설정(이름, 카테고리 그룹, 기능 여부) 전체 리스트 조회
     * @return
     */
    List<BoardMasterResponse> getBoardConfigList();

    /**
     * 게시판 설정 등록
     * @param request
     */
    void addBoardMaster(BoardMasterRequest request);
}
