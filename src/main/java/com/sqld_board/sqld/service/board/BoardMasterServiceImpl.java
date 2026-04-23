package com.sqld_board.sqld.service.board;

import com.sqld_board.sqld.dto.request.board.BoardMasterRequest;
import com.sqld_board.sqld.dto.response.board.BoardMasterResponse;
import com.sqld_board.sqld.dto.response.code.CategoryResponse;
import com.sqld_board.sqld.exception.board.ExistsBoardCodeException;
import com.sqld_board.sqld.exception.board.ExistsBoardNameException;
import com.sqld_board.sqld.exception.board.NotFoundBoardCodeException;
import com.sqld_board.sqld.exception.code.ExistsCommonGroupCodeException;
import com.sqld_board.sqld.exception.code.NotFoundGroupCodeException;
import com.sqld_board.sqld.mapper.BoardMasterMapper;
import com.sqld_board.sqld.mapper.CommonCodeGroupMapper;
import com.sqld_board.sqld.model.board.BoardMaster;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardMasterServiceImpl implements BoardMasterService{

    private final BoardService boardService;

    private final BoardMasterMapper boardMasterMapper;
    private final CommonCodeGroupMapper commonCodeGroupMapper;

    @Override
    public void updateBoardMaster(String boardCode, BoardMasterRequest request) {
        // 1. boardCode 존재 확인
        validateBoardCodeExists(boardCode);

        // 2. groupCode 존재 확인
        validateGroupCodeExists(request.getGroupCode());

        // 3, boardName 확인
        existsByBoardNameExceptMe(request.getBoardName(),boardCode);

        // 3. build
       BoardMaster boardMasterData =  BoardMaster.builder()
                                                  .boardCode(boardCode)
                                                  .boardName(request.getBoardName())
                                                  .useYn(request.getUseYn())
                                                  .fileYn(request.getFileYn())
                                                  .replyYn(request.getReplyYn())
                                                  .build();
       // 4. update 실행
            boardMasterMapper.updateBoardMaster(boardMasterData);
    }

    /**
     * 특정 게시판의 설정(이름, 카테고리 그룹, 기능 여부) 상세 조회
     * @param boardCode
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public BoardMasterResponse readBoardMasterDetail(String boardCode) {
        BoardMaster boardMasterData  = boardMasterMapper.readBoardMasterDetail(boardCode).orElseThrow(NotFoundBoardCodeException::new);
            List<CategoryResponse> categories = boardService.getCategoryListByBoardCode(boardCode);

            return BoardMasterResponse.of(boardMasterData,categories);
    }

    /**
     *특정 게시판의 설정(이름, 카테고리 그룹, 기능 여부) 전체 리스트 조회
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<BoardMasterResponse> getBoardConfigList() {
       // 1. 전체 게시판 마스터 목록 조회
        List<BoardMaster>  boardConfigList =  boardMasterMapper.getBoardConfigList();

        // 2. 각 마스터 정보에 카테고리 시리스트 매핑
        return boardConfigList.stream().map(list ->{
            List<CategoryResponse> categories = boardService.getCategoryListByBoardCode(list.getBoardCode());

            // 수정된 of () 호출 조힙
            return BoardMasterResponse.of(list, categories);
        }).collect(Collectors.toList());

//       return boardConfigList.stream()
//                             .map(BoardMasterResponse::of)
//                             .collect(Collectors.toList());
    }

    /**
     * 새로운 게시판 설정 등록
     * @param request
     */
    @Override
    public void addBoardMaster(BoardMasterRequest request) {

        // 1. boardCode 유무 확인
        validateDuplicateBoardCodeExist(request.getBoardCode());

        // 2. groupCode 유무 확인
        validateGroupCodeExists(request.getGroupCode());

        // 3. builder dto -> model
        BoardMaster boardMasterData = BoardMaster.builder()
                        .groupCode(request.getGroupCode())
                        .boardName(request.getBoardName())
                        .useYn(request.getUseYn() !=null ? request.getUseYn() : "Y")
                        .fileYn(request.getFileYn() != null ? request.getFileYn() : "Y")
                        .replyYn(request.getReplyYn() != null ? request.getReplyYn() : "Y")
                        .build();
        boardMasterMapper.addBoardMaster(boardMasterData);
    }



    /* ========================================================================= */
    /*                          private method */
    /* ========================================================================= */

    private void existsByBoardNameExceptMe(String boardName, String boardCode){
    if(boardMasterMapper.existsByBoardNameExceptMe(boardName, boardCode)) {
        throw new ExistsBoardNameException();
    }
    }

    /**
     * 그룹 코드 존재 여부를 확인한다.
     * @param groupCode
     */
    private void validateGroupCodeExists(String groupCode){
        if(!commonCodeGroupMapper.existGroupCode(groupCode)){
            throw new NotFoundGroupCodeException();
        }
    }

    /**
     * 그룹 코드 중복 여부를 검증한다.
     * @param groupCode
     */
    private void validateDuplicationGroupCode(String groupCode) {
        if(commonCodeGroupMapper.existGroupCode(groupCode)){
            throw new ExistsCommonGroupCodeException();
        }
    }

    /**
     * boarderCode 존재 여부를 검증한다.
     * @param boardCode
     */
    private void validateBoardCodeExists(String boardCode) {
        if(!boardMasterMapper.validateBoardCodeExist(boardCode)){
            throw new NotFoundBoardCodeException();
        }
    }

    /**
     *  boardCode 중복 여부를 검증한다.
     * @param boardCode
     */
    private void validateDuplicateBoardCodeExist(String boardCode) {
        if(boardMasterMapper.validateBoardCodeExist(boardCode)){
            throw new ExistsBoardCodeException();
        }
    }

}
