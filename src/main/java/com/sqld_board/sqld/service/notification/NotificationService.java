package com.sqld_board.sqld.service.notification;

import com.sqld_board.sqld.dto.response.webSocket.NotificationResponse;

import java.util.List;

public interface NotificationService {

    /**
     * 읽음 확인
     * @param notiId
     */
    void updateNotiRead(Long notiId);

    /**
     * 알림 내역 조회
     * @param receiverId
     * @return
     */
    List<NotificationResponse> searchNotification(String receiverId);
}
