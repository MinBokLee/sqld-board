package com.sqld_board.sqld.mapper;

import com.sqld_board.sqld.model.notification.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NotificationMapper {

    //3 30일 지난 알림 삭제
    int deleteOldNotifications();

    // 알림 조회 (프론트에 알림 목록 보여줄 때, 사용)
    List<Notification> searchNotification(@Param("receiverId") String receiverId);

    // 알림 읽음 처리 (선택사항)
    void updateNotiRead(long notiId);
}
