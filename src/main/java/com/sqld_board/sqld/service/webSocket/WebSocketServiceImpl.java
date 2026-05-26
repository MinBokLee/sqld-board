package com.sqld_board.sqld.service.webSocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqld_board.sqld.dto.request.websocket.RealTimeMessage;
import com.sqld_board.sqld.mapper.MemberMapper;
import com.sqld_board.sqld.mapper.WebSocketMapper;
import com.sqld_board.sqld.model.websocket.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    private final WebSocketMapper webSocketMapper;

    private final MemberMapper memberMapper;

    @Value("${spring.data.redis.topic}")
    private String redisTopic;

    /*private static final String PRESENCE_KEY_PREFIX = "chat:presence:";*/

    private String getPresenceKey(String roomId) {
        return "chat:presence:" + redisTopic + ":" + roomId;
    }

    @Override
    public void addUser(String roomId, String nickName) {
//        String key = PRESENCE_KEY_PREFIX + roomId;
        String key = getPresenceKey(roomId);
        redisTemplate.opsForSet().add(key, nickName);
        log.info("[Presence] User {} joined room {}", nickName, roomId);
    }

    @Override
    public void removeUser(String roomId, String nickName) {
//        String key = PRESENCE_KEY_PREFIX + roomId;
        String key = getPresenceKey(roomId);
        redisTemplate.opsForSet().remove(key, nickName);
        log.info("[Presence] User {} left room {}", nickName, roomId);
    }

    @Override
    public Set<Object> getConnectedUsers(String roomId) {
//        String key = PRESENCE_KEY_PREFIX + roomId;
        String key = getPresenceKey(roomId);
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public Long getConnectedUserCount(String roomId) {
//        String key = PRESENCE_KEY_PREFIX + roomId;
        String key = getPresenceKey(roomId);
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public void processAndSend(RealTimeMessage rtMessage) throws JsonProcessingException {
        // 발신자 닉네임 조회
        memberMapper.readMemberByMemberId(rtMessage.getSenderId())
                                                   .ifPresent(member -> {
                                                       rtMessage.setSenderName(member.getUserName());
                                                   });

        // 입장 메시지 처리
        if(rtMessage.getType() == RealTimeMessage.MessageType.ENTER){
            rtMessage.setContent(rtMessage.getSenderName() + "님이 입장하셨습니다.");
        }

        rtMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        ChatMessage chatMsg = ChatMessage.builder()
                                         .roomId(rtMessage.getRoomId())
                                         .senderName(rtMessage.getSenderName())
                                         .senderId(rtMessage.getSenderId())
                                         .content(rtMessage.getContent())
                                         .chatType(rtMessage.getType().name())
                                         .sendTime(LocalDateTime.now())
                                         .build();

        webSocketMapper.insertChatMessage(chatMsg);

        String jsonMessage = objectMapper.writeValueAsString(rtMessage);
        log.info("JSON Message to Redis: {}", jsonMessage);
        redisTemplate.convertAndSend(redisTopic, jsonMessage);
    }
}
