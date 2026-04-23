package com.sqld_board.sqld.model.websocket;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChatMessage {

    private long messageId;

    private String senderName;          // 발신자 명

    private String roomId;

    private String senderId;            // 발신인 아이디

    private String content;             // 메세지 내용

    private LocalDateTime sendTime;    // 보낸 시간

    private String chatType;           // 메시지 타입 (TALK, ENTER, LEAVE 등)
}
