package com.sqld_board.sqld.controller.notificationStomp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqld_board.sqld.dto.request.websocket.RealTimeMessage;
import com.sqld_board.sqld.service.notificationStomp.NotificationStompService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 실시간 전송 전문 (웹소켓을 통해 실시간으로 날아오는 알림 메시지 처리를 담당한다.)
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class NotificationStompController {

    private final NotificationStompService notificationStompService;

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper; // [JSON 변환기  - 객체-> String ]

    @Value("${redis.topic}")
    private String redisTopic;

    @Operation(summary = "알림 저장")
    @MessageMapping("/notification")
    public void notification(RealTimeMessage message) throws  Exception{
        // 1. 서버 시간 등 공통 설정
        message.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        message.setType(RealTimeMessage.MessageType.NOTIFY);

        // 2. service 호출
        notificationStompService.saveNotification(message);

        // 3. objectMapper.writeValueAsString 를 사용하여, 객체를 문자열로 변환하여 Redis 로 발행한다.
        // 채팅 메세지에서 string 형식으로 되어 있기 때문에 동일하게 맞춰야 RedisSubscriber 가 에러 없이 읽는다.
        String jsonMessage = objectMapper.writeValueAsString(message);
        log.info("JSON Message: {}", jsonMessage);

        //3. [전송] Redis 로 발행 (실시간 알림 쓰기)
        redisTemplate.convertAndSend(redisTopic, jsonMessage);

    }
}
