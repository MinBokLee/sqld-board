package com.sqld_board.sqld.controller.webSocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqld_board.sqld.dto.request.websocket.RealTimeMessage;
import com.sqld_board.sqld.mapper.MemberMapper;
import com.sqld_board.sqld.mapper.WebSocketMapper;
import com.sqld_board.sqld.model.websocket.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Controller
public class WebSocketController {

    private final RedisTemplate<String, Object> redisTemplate; // ValueSerializer 가  String 일 때)

    private final ObjectMapper objectMapper; // [추가] JSON 변환기

    private final WebSocketMapper webSocketMapper;

    private final MemberMapper memberMapper;

    /**
     * 메시지 저장
     * 클라이언트가 /pub/chat/message 로 메세지를 보내면 호출된다.
     * @param message
     */
    @MessageMapping("/chat/message")
    public void message(RealTimeMessage message) throws Exception{

        //[추가] 발신자 닉네임 조회 (DB에서 가져오기)
        memberMapper.readMemberByMemberId(message.getSenderId()).ifPresent(member -> {
            message.setSenderName(member.getUserName());
        });

        //[추가] 입장(ENTER) 메시지 처리
        if(message.getType() == RealTimeMessage.MessageType.ENTER){
            // 서버에서 직접 입장 문구 작성
            message.setContent(message.getSenderName() + "님이 입장하셨습니다.");
        }

        // 1. 시간 설정 (서버 시간 기준)
        message.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        ChatMessage chatMsg = ChatMessage.builder()
                        .roomId(message.getRoomId())
                        .senderName(message.getSenderName())
                        .senderId(message.getSenderId())
                        .content(message.getContent())
                        .chatType(message.getType().name())  // ENUM 을 String 으로 변환
                        .sendTime(LocalDateTime.now())
                        .build();

        webSocketMapper.insertChatMessage(chatMsg); // DB 저장 실행;

        // [핵심] 객체를 JSON 문자열로 직접 변환
        // 이렇게 하면 RedisTemplate의 ValueSerializer(String)와 완벽히 호환
        // RealTimeMessage 객체를 문자열로 만든 뒤, Redis로 던져준다.
        String jsonMessage = objectMapper.writeValueAsString(message);

        log.info("JSON Message to Redis: {}", jsonMessage);

        //log.info("Message Reviced from /pub/chat/message: {}", message.getContent());

        // 2. Redis 의 'realtime' 토픽으로 메시지 발행(Publish)
        // 그러면 RedisSubscribe 가 이를 감지하여 /sub/... 채널 구독자들에게 전달한다.
        // 변환된 문자열을 던진다 (조회수 로직에 영향 없음!)
        redisTemplate.convertAndSend("realtime", jsonMessage);
    }

} //end of WebSocketController();
