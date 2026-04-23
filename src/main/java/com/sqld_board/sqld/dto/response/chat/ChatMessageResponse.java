package com.sqld_board.sqld.dto.response.chat;

import com.sqld_board.sqld.model.websocket.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChatMessageResponse {
    private long messageId;

    private String roomId;

    private String senderName;          // 발신자 명

    private String senderId;            // 보낸 사람

    private String content;             // 메세지 내용

    private LocalDateTime sendTime;    // 보낸 시간

    private String chatType;           // 메시지 타입 (TALK, ENTER, LEAVE 등)

    public static ChatMessageResponse modelToDto(ChatMessage message){

        return  ChatMessageResponse.builder()
                .messageId(message.getMessageId())
                .roomId(message.getRoomId())
                .senderName(message.getSenderName())
                .content(message.getContent())
                .sendTime(message.getSendTime())
                .chatType(message.getChatType())
                .build();
        }
}
