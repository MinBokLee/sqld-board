package com.sqld_board.sqld.service.redisConfig;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqld_board.sqld.dto.request.websocket.RealTimeMessage;
import com.sqld_board.sqld.dto.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis 로부터 받은 원시 데이터(JSON 문자열)를 자바 객체로 바꾸고,
 * 이를 다시 브라우저(WebSocket)로 보냄.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // 1. Redis에서 온 데이터를 String으로 변환
            String publishMessage = new String(message.getBody());

            // 2. JSON 문자열을 RealTimeMessage 객체로 역직렬화
            RealTimeMessage rtMessage = objectMapper.readValue(publishMessage, RealTimeMessage.class);

            log.info("Redis Subscribe - Message Received: {}", publishMessage);

            // 3. 메시지 타입에 따른 웹소켓 라우팅(브라우저로 전송)
            handleRouting(rtMessage);
        } catch (Exception e){
            log.error("Redis 메시지 역직렬화 실패: {}", e.getMessage());
        }
    }

    /**
     * 메시지 타입에 따라 브라우저의 적절한 구독 경로로 메시지를 보냄
     */
    private void handleRouting(RealTimeMessage rtMessage){
        RealTimeMessage.MessageType type = rtMessage.getType();

        // [A] 채팅 관련 (입장, 퇴장, 일반 대화)
        if(type == RealTimeMessage.MessageType.TALK  ||
           type == RealTimeMessage.MessageType.ENTER ||
           type == RealTimeMessage.MessageType.QUIT) {

            // 모든 채팅 참여자에게 전달 (경로 수정: / 추가 및 Response 래핑)
            Map<String, Object> customResponse = new HashMap<>();
            customResponse.put("success", true);
            customResponse.put("code", 200);
            customResponse.put("msg", "Success");
            customResponse.put("data", rtMessage); // result 없이 바로 data에 할당

            messagingTemplate.convertAndSend("/sub/chat/room/" + rtMessage.getRoomId(), customResponse);
        }
        // [B] 개인 알림 (특정 사용자 타겟)
        else if(type == RealTimeMessage.MessageType.NOTIFY) {
            if(rtMessage.getTargetId() != null){
                // 알림 전달 (경로 수정: / 추가 및 Response 래핑)
                Map<String, Object> customResponse = new HashMap<>();
                customResponse.put("success", true);
                customResponse.put("code", 200);
                customResponse.put("msg", "Success");
                customResponse.put("data", rtMessage); // result 없이 바로 data에 할당

                messagingTemplate.convertAndSend("/sub/user/" + rtMessage.getTargetId(), customResponse);
            }
        }
    }
}
