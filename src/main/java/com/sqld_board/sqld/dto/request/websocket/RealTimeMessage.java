package com.sqld_board.sqld.dto.request.websocket;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RealTimeMessage {

    private Long notiId;            // [추가] 알림 고유 ID (DB PK)

    private String senderName;      // 발신자 명

    // 메세지 타입 구분
    public  enum MessageType {
        ENTER   //채팅방 입장
       ,TALK    // 일반 채팅 메시지
       ,NOTIFY  // 알림(게시글 댓글 등)
       ,QUIT    // 채팅방 퇴장
    }

    private MessageType type;   // 메시지 타입

    private String roomId;      // 오픈채팅방 ID (오픈채팅용, 예: "OPEN_ROOM")

    private String senderId;    // 보낸 사람 ID (memberId)

    private String targetId;    // 받는 사람 ID (개인 메시지/ 알림용)

    private String content;     // 메시지 내용

    private String timestamp;   // 보낸 시간 ( 예: 2026-04-06 16:10)

    // 알림 클릭 시 이동할 URL( 알림용 추가 필드)
    private String targetUrl;

}
