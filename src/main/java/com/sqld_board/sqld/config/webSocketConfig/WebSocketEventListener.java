package com.sqld_board.sqld.config.webSocketConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqld_board.sqld.dto.request.websocket.RealTimeMessage;
import com.sqld_board.sqld.dto.response.Response;
import com.sqld_board.sqld.handler.ResponseHandler;
import com.sqld_board.sqld.mapper.MemberMapper;
import com.sqld_board.sqld.service.webSocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

 @Slf4j @Component @RequiredArgsConstructor
public class WebSocketEventListener {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    private final WebSocketService webSocketService;
    private final SimpMessagingTemplate messagingTemplate;

    // 1. 입장(구독) 감지
    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();

        // 채팅방 구독 경로인지 확인 (예: /sub/chat/room1)
        if (destination != null && destination.startsWith("/sub/chat/room/")) {
            String roomId = destination.replace("/sub/chat/room/", "").replace("/presence", "");
            Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

            if (sessionAttributes != null) {
                String senderName = (String) sessionAttributes.get("senderName");

                 if (!destination.endsWith("/presence")) {
                    if (senderName != null) {
                        //Redis에 접속자 추가
                        webSocketService.addUser(roomId, senderName);
                        // 세션에 현재 방 ID 저장 (Disconnect 시 사용)
                        sessionAttributes.put("currentRoomId", roomId);
                        log.info("[Presence] User {} joined room {}", senderName, roomId);
                    }
                }
                // 명단 브로드캐스트
                broadcastPresence(roomId);
            }
        }
    }

    // 2. 퇴장 감지 (기존 코드 보완)
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

        if(sessionAttributes != null) {
            if(sessionAttributes.containsKey("alreadyLeft")) {
                return;
            }

            String senderId = (String) sessionAttributes.get("senderId");
            String senderName = (String) sessionAttributes.get("senderName");
            String roomId = (String) sessionAttributes.get("currentRoomId"); // 세션에서 방 ID 꺼내기

            if(senderId != null && senderName !=null){
                sessionAttributes.put("alreadyLeft", true);

                // 추가 Redis 에서 접속자 제거 및 명단 알림
                if(roomId != null){
                    webSocketService.removeUser(roomId,senderName);
                    broadcastPresence((roomId)); // 공통 명단 전송
                }

                // 기존 퇴장 메시지 발송 로직
                try {
                    RealTimeMessage leaveMessage = RealTimeMessage.builder()
                            .type(RealTimeMessage.MessageType.QUIT)
                            .roomId(roomId !=null ? roomId : "OPEN_CHAT")
                            .senderId(senderId)
                            .senderName(senderName)
                            .content(senderName + "님이 퇴장하셨습니다.")
                            .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .build();

                    String josonMessage = objectMapper.writeValueAsString(leaveMessage);
                    redisTemplate.convertAndSend("realtime", josonMessage);
                } catch (Exception e) {
                    log.error("퇴장 메시지 전송 실패: {}", e.getMessage());
                }
            }
        }
    }


    // 공통 명단 전송
    private void broadcastPresence(String roomId){
        Map<String,Object> presenceData = new HashMap<>();
        presenceData.put("roomId", roomId);
        presenceData.put("userList", webSocketService.getConnectedUsers(roomId));
        presenceData.put("userCount", webSocketService.getConnectedUserCount(roomId));

        // /sub/chat/room/{roomId}/presence 경로로 전송
        messagingTemplate.convertAndSend("/sub/chat/room/" + roomId +  "/presence"
                                        , Response.success(presenceData));

        log.info("[Presence] Room {}  updated. Count: {}", roomId, presenceData.get("userCount"));
    }

} // end of WebSocketEventListener()

//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String sessionId = headerAccessor.getSessionId();
//        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
//
//        if(sessionAttributes != null){
//            //[중복 방지] 이 세션에서 이미 퇴장 처리를 했는지 확인
//            if(sessionAttributes.containsKey("alreadyLeft")){
//                return; // 이미 처리 되었다면 여기서 즉시 종료 ! ( 두 번째 호출 무시)
//            }
//            // 1. 세션에서 미지 저장한 정보 꺼내기
//            String senderId = (String) sessionAttributes.get("senderId");
//            String senderName = (String) sessionAttributes.get("senderName");
//
//            // 2. 정보가 있는 경우에만 메시지 발송
//            if(senderId != null && senderName != null){
//                //[마킹] 퇴장 처리 완료 표시를 남긴다
//                sessionAttributes.put("alreadyLeft", true);
//
//                log.info("사용자 퇴장 감지 (세션 기반): {}", senderId, sessionId);
//
//                // 3. 퇴장 메시지(QUIT 생성)
//                try {
//                    RealTimeMessage leaveMessage = RealTimeMessage.builder()
//                            .type(RealTimeMessage.MessageType.QUIT)
//                            .roomId("OPEN_CHAT")
//                            .senderId(senderId)
//                            .senderName(senderName)
//                            .content(senderName + "님이 퇴장하셨습니다.")
//                            .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//                            .build();
//
//                    // 4. Redis로 발생 (채널명 "realtime 확인!")
//                    String jsonMessage = objectMapper.writeValueAsString(leaveMessage);
//                    redisTemplate.convertAndSend("realtime", jsonMessage);
//                } catch (Exception e) {
//                    log.error("퇴장 메시지 전송 실패: {}" , e.getMessage());
//                }
//            }
//        }
//
//    }
//}