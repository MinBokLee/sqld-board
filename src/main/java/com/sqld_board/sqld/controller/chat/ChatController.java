package com.sqld_board.sqld.controller.chat;

import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.chat.ChatMessageResponse;
import com.sqld_board.sqld.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary="채팅방 히스토리 조회")
    @GetMapping("/getChatHistory/{roomId}")
    public ResponseEntity<Response> getChatHistory(@PathVariable String roomId) {

       List<ChatMessageResponse> chatDate = chatService.getChatHistory(roomId);

       return ResponseEntity.ok(Response.success(chatDate));
    }

}
