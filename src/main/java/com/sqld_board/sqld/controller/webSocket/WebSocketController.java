package com.sqld_board.sqld.controller.webSocket;

import com.sqld_board.sqld.dto.request.websocket.RealTimeMessage;
import com.sqld_board.sqld.service.webSocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class WebSocketController {

    private final WebSocketService webSocketService;

    /**
     * 메시지 저장
     * 클라이언트가 /pub/chat/message 로 메세지를 보내면 호출된다.
     * @param message
     */
    @MessageMapping("/chat/message")
    public void message(RealTimeMessage message) throws Exception {

        webSocketService.processAndSend(message);
    }
} //end of WebSocketController();
