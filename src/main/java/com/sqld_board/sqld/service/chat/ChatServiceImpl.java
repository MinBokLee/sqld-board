package com.sqld_board.sqld.service.chat;

import com.sqld_board.sqld.dto.response.chat.ChatMessageResponse;
import com.sqld_board.sqld.mapper.ChatMapper;
import com.sqld_board.sqld.model.websocket.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService{

    private final ChatMapper chatMapper;

    @Override
    public List<ChatMessageResponse> getChatHistory(String roomId) {
       List<ChatMessage> chatData =  chatMapper.getChatHistory(roomId);

        return chatData.stream()
                .map(ChatMessageResponse::modelToDto)
                .collect(Collectors.toList());
    }
}
