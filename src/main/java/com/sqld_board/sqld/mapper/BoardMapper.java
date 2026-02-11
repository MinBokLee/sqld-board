package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.model.Board;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BoardMapper {
    List<Board> getBoardList();
}
