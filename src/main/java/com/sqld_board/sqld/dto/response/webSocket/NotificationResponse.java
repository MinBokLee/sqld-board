package com.sqld_board.sqld.dto.response.webSocket;

import com.sqld_board.sqld.model.notification.Notification;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private long notiId;

    private String receiverId;      // 알림을 받을 사람

    private String senderId;        // 알림을 유발한 사람 (선택)

    private String notiType;        // 알림 종류(COMMENT, LIKE, NOTICE 등)

    private String message;         // 알림 내용

    private String isRead;          // 읽음 여부(Y/N)

    private LocalDateTime regDate;  // 발생 시간

    private String targetUrl;       // 클릭 시 이동할 페이지 주소


    public static NotificationResponse modelToDto(Notification noti) {
        return NotificationResponse.builder()
                .notiId(noti.getNotiId())
                .receiverId(noti.getReceiverId())
                .senderId(noti.getSenderId())
                .notiType(noti.getNotiType())
                .message(noti.getMessage())
                .isRead(noti.getIsRead())
                .regDate(noti.getRegDate())
                .targetUrl(noti.getTargetUrl())
                .build();
    }
}
