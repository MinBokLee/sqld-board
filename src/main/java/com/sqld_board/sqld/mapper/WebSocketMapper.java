package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.model.websocket.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WebSocketMapper {

    //  채팅 저장
    void insertChatMessage(ChatMessage chatMessage);

}

