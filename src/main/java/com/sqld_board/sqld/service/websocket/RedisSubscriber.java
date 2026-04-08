package com.sqld_board.sqld.service.websocket;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqld_board.sqld.dto.request.websocket.RealTimeMessage;
import com.sqld_board.sqld.mapper.WebSocketMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

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
            RealTimeMessage rtMessage = objectMapper.readValue(publishMessage, RealTimeMessage.class);

            //[A] 오픈 채팅 메시지 처리
            if (rtMessage.getType() == RealTimeMessage.MessageType.TALK) {
                messagingTemplate.convertAndSend("/sub/chat/open", rtMessage);

                //[B] 개인 알림/메시지 처리 (targetId가 있는 경우)
            } else if (rtMessage.getType() == RealTimeMessage.MessageType.NOTIFY) {
                // 해당 사용자의 전용 채널로 발송
                messagingTemplate.convertAndSend("/sub/user/" + rtMessage.getTargetId(), rtMessage);

                // [C] 전체 알림 처리
            } else if (rtMessage.getTargetId() == null || "ALL".equals(rtMessage.getTargetId())) {
                messagingTemplate.convertAndSend("/sub/all", rtMessage);
            }
            log.info("RedisMessage Distributed: {}", publishMessage);

        } catch (Exception e){
            log.error("Redis 메시지 역질렬화 실패: {}", e.getMessage());
        }

            // 2. 메시지를 객체로 변환 ( 예: ChatMessage 객체 - 나중에 생성)
            // ChatMessage = chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);

            //3. WebSocket 구독자들에게 메시지 전달
            // 예: /sub/chat/room/1 주소를 구독 중인 사람들에게 전달
            // messagingTemplate.convertAndSend("/sub/chat/room/" + chaMessage.getRoomId(), chatMessage);

    } //end of onMessage();
}
