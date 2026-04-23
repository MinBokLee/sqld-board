package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.model.websocket.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WebSocketMapper {


    List<ChatMessage> getCHatHistory(@Param("roomId") String roomId);
    //  채팅 저장
    void insertChatMessage(ChatMessage chatMessage);

}

