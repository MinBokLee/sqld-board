package com.sqld_board.sqld.controller.board;

import com.sqld_board.sqld.dto.request.board.BoardMasterRequest;
import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.board.BoardMasterResponse;
import com.sqld_board.sqld.exception.MessageType;
import com.sqld_board.sqld.handler.ResponseHandler;
import com.sqld_board.sqld.service.board.BoardMasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/boardMaster")
@RequiredArgsConstructor
public class BoardMasterController {

    private final BoardMasterService boardMasterService;
    private final ResponseHandler responseHandler;

    /**
     * 게시판 설정 업데이트
     * @param boardCode
     * @param request
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PatchMapping("/updateBoardMaster/{boardCode}")
    public Response updateBoardMaster(@PathVariable String boardCode, @RequestBody  BoardMasterRequest request){
        boardMasterService.updateBoardMaster(boardCode, request);

        return responseHandler.getSuccessResponse(MessageType.UPDATE_BOARD_MASTER_SUCCESS);
    }
    /**
     * 특정 게시판의 설정(이름, 카테고리 그룹, 기능 여부) 상세 조회
     * @param boardCode
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/readBoardMasterDetail/{boardCode}")
    public Response readBoardMasterDetail(@PathVariable String boardCode) {
        BoardMasterResponse boardMasterData = boardMasterService.readBoardMasterDetail(boardCode);

        return Response.success(boardMasterData);
    }

    /**
     * 특정 게시판의 설정(이름, 카테고리 그룹, 기능 여부) 전체 리스트 조회
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getBoardConfigList")
    public Response getBoardConfigList() {
        List<BoardMasterResponse> boardConfigList =  boardMasterService.getBoardConfigList();
        return Response.success(boardConfigList);
    }

    /**
     * 게시판 설정 등록
     * @param request
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/addBoardMaster")
    public Response addBoardMaster(@RequestBody BoardMasterRequest request){

        boardMasterService.addBoardMaster(request);

        return responseHandler.getSuccessResponse(MessageType.ADD_BOARD_MASTER_SUCCESS);
    }

}
