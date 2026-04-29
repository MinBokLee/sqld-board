package com.sqld_board.sqld.service.webSocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sqld_board.sqld.dto.request.websocket.RealTimeMessage;
import java.util.Set;

public interface WebSocketService {

    void processAndSend(RealTimeMessage rtMessage) throws JsonProcessingException;

    // 접속자 관리 메서드
    void addUser(String roomId, String nickname);
    void removeUser(String roomId, String nickname);
    Set<Object> getConnectedUsers(String roomId);
    Long getConnectedUserCount(String roomId);

}
