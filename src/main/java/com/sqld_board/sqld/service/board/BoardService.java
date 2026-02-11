package com.sqld_board.sqld.service.board;

import com.sqld_board.sqld.dto.response.board.BoardResponse;
import com.sqld_board.sqld.mapper.BoardMapper;
import com.sqld_board.sqld.model.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;

    public List<BoardResponse> getBoardList() {
        List<Board> boards = boardMapper.getBoardList();
        return boards.stream()
                .map(BoardResponse::new)
                .collect(Collectors.toList());
    }
}
