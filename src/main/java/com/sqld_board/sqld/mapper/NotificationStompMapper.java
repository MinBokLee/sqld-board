package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.model.notification.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface NotificationStompMapper {

    // 알림 저장
    void saveNotification(Notification notification);
}
