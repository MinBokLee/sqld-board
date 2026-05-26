package com.sqld_board.sqld.service.notificationStomp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqld_board.sqld.dto.request.notification.NotificationRequest;
import com.sqld_board.sqld.dto.request.websocket.RealTimeMessage;
import com.sqld_board.sqld.mapper.NotificationStompMapper;
import com.sqld_board.sqld.model.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationStompServiceImpl implements NotificationStompService {

    private final NotificationStompMapper notificationStompMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.topic}")
    private String redisTopic;

    /**
     * 알림 발송
     * @param message
     */
    @Override
    public void sendNotification(RealTimeMessage message) {
        try {
            // 1. DB 저장 (messsage에 notiId가 셋팅됨)
            saveNotification(message);

            // 2. Redis 발행(실시간 전송 트리거) notiId가 포함된 상태
            // RealTimeMessage 객체를 JSON 문자열로 변환하여 'realtime' 토픽으로 전송
            String jsonMessage = objectMapper.writeValueAsString(message);
            redisTemplate.convertAndSend(redisTopic, jsonMessage);

            log.info("Notification sent: {}", jsonMessage);
        } catch (Exception e) {
            log.error("알림 발송 중 오류 발생: {}", e.getMessage());
        }
    }

    /**
     * 알림 저장
     * @param message
     */
    @Override
    public void saveNotification(RealTimeMessage message) {
        // 1. Request DTO 생성
        NotificationRequest request = NotificationRequest.builder()
                .receiverId(message.getTargetId())
                .senderId(message.getSenderId())
                .notiType(message.getType().name())
                .message(message.getContent())
                .targetUrl(message.getTargetUrl()) // DTO의 URL을 매핑
                .isRead("N")
                .createAt(LocalDateTime.now())
                .build();

        // 2. [모델 변환] toModel 호출
        Notification model = NotificationRequest.toModel(request);

        // 3. [저장] (model 객체의 notiId 필드에 값이 들어간다)
        notificationStompMapper.saveNotification(model);

        // 4. 생성된 ID를 실시시간 메시지에 다시 넣어줌!
        message.setNotiId(model.getNotiId());
    }


}
