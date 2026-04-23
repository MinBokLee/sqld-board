package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.model.websocket.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ChatMapper {

    List<ChatMessage> getChatHistory(@Param("roomId") String roomId);
}
