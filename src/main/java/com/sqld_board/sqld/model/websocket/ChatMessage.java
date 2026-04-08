package com.sqld_board.sqld.model.websocket;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

    private long messageId;

    private String senderId;            // 보낸 사람

    private String content;             // 메세지 내용

    private LocalDateTime sendTime;    // 보낸 시간

    private String chatType;           // 메시지 타입 (TALK, ENTER, LEAVE 등)
}
