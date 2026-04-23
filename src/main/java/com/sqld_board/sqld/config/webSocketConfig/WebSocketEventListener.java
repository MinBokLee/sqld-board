package com.sqld_board.sqld.config.webSocketConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqld_board.sqld.dto.request.websocket.RealTimeMessage;
import com.sqld_board.sqld.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

        if(sessionAttributes != null){
            //[중복 방지] 이 세션에서 이미 퇴장 처리를 했는지 확인
            if(sessionAttributes.containsKey("alreadyLeft")){
                return; // 이미 처리 되었다면 여기서 즉시 종료 ! ( 두 번째 호출 무시)
            }
            // 1. 세션에서 미지 저장한 정보 꺼내기
            String senderId = (String) sessionAttributes.get("senderId");
            String senderName = (String) sessionAttributes.get("senderName");

            // 2. 정보가 있는 경우에만 메시지 발송
            if(senderId != null && senderName != null){
                //[마킹] 퇴장 처리 완료 표시를 남긴다
                sessionAttributes.put("alreadyLeft", true);

                log.info("사용자 퇴장 감지 (세션 기반): {}", senderId, sessionId);

                // 3. 퇴장 메시지(QUIT 생성)
                try {
                    RealTimeMessage leaveMessage = RealTimeMessage.builder()
                            .type(RealTimeMessage.MessageType.QUIT)
                            .roomId("OPEN_CHAT")
                            .senderId(senderId)
                            .senderName(senderName)
                            .content(senderName + "님이 퇴장하셨습니다.")
                            .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .build();

                    // 4. Redis로 발생 (채널명 "realtime 확인!")
                    String jsonMessage = objectMapper.writeValueAsString(leaveMessage);
                    redisTemplate.convertAndSend("realtime", jsonMessage);
                } catch (Exception e) {
                    log.error("퇴장 메시지 전송 실패: {}" , e.getMessage());
                }
            }
        }

    }
}
