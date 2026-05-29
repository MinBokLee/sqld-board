package com.sqld_board.sqld.service.notification;

import com.sqld_board.sqld.dto.response.webSocket.NotificationResponse;
import com.sqld_board.sqld.mapper.NotificationMapper;
import com.sqld_board.sqld.model.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    @Override
    @Transactional
    public void updateAllNotiRead(String receiverId) {
        notificationMapper.updateAllNotiRead(receiverId);
    }

    @Override
    @Transactional
    public void updateNotiRead(Long notiId) {
        notificationMapper.updateNotiRead(notiId);
    }

    @Override
    public List<NotificationResponse> searchNotification(String receiverId) {

        List<Notification> notiList = notificationMapper.searchNotification(receiverId);
        return notiList.stream()
                       .map(NotificationResponse::modelToDto)
                       .collect(Collectors.toList());
    }
}
