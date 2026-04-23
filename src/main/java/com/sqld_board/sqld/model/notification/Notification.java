package com.sqld_board.sqld.model.notification;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Notification {

    private long notiId;

    private String receiverId;      // 알림을 받을 사람

    private String senderId;        // 알림을 유발한 사람 (선택)

    private String notiType;        // 알림 종류(COMMENT, LIKE, NOTICE 등)

    private String message;         // 알림 내용

    private String isRead;          // 읽음 여부(Y/N)

    private LocalDateTime createAt;  // 발생 시간

    private String targetUrl;       // 클릭 시 이동할 페이지 주소
}
