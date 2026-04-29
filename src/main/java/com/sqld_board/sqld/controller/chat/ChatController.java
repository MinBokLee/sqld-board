package com.sqld_board.sqld.controller.chat;

import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.dto.response.chat.ChatMessageResponse;
import com.sqld_board.sqld.handler.ResponseHandler;
import com.sqld_board.sqld.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final ResponseHandler responseHandler;



    @Operation(summary="채팅방 히스토리 조회")
    @GetMapping("/getChatHistory/{roomId}")
    public ResponseEntity<Object> getChatHistory(@PathVariable String roomId) {

       List<ChatMessageResponse> chatHistory = chatService.getChatHistory(roomId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("code", 200);
        response.put("msg", "success");
        response.put("data", chatHistory); // result 계층 제거

        return ResponseEntity.ok()
                            .cacheControl(CacheControl.noStore().mustRevalidate())
                            .body(response);
               // noStore() : 브라우저 저장을 원천 차단 (보안이 최우선)
               // noCache(): 브라우저가 데이터를 저장할 수는 있지만, 사용할 때마다 "서버에 이 데이터가 아직 유효한가요?"라고 재검사를 요청.

    }

}
