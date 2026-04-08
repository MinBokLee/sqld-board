package com.sqld_board.sqld.service.notificationStomp;

import com.sqld_board.sqld.dto.request.websocket.RealTimeMessage;

public interface NotificationStompService {

    void sendNotification(RealTimeMessage message);

    void saveNotification(RealTimeMessage message);
}
