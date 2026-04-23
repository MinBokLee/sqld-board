package com.sqld_board.sqld.service.chat;

import com.sqld_board.sqld.dto.response.chat.ChatMessageResponse;

import java.util.List;

public interface ChatService {

    List<ChatMessageResponse> getChatHistory(String roomId);
}
