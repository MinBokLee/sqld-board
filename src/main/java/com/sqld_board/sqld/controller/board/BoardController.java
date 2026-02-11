package com.sqld_board.sqld.controller.board;


import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.board.BoardResponse;
import com.sqld_board.sqld.service.board.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시판 목록 읽기")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/list")
    public Response getBoardList() {
        List<BoardResponse> result = boardService.getBoardList();
        return Response.success(result);
    }
}
