package com.sqld_board.sqld.common.scheduler;

import com.sqld_board.sqld.mapper.NotificationMapper;
import com.sqld_board.sqld.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 실시간 알림 관련 scheduler
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationMapper notificationMapper;

    /**
     * 매일 새벽 4시에 실행되어 30일이 지난 알림을 삭제 한다.
     * 크론 표현식: 초 분 시 일 월 요일
     */
    @Transactional
    @Scheduled(cron = "0 0 4 * * *")
    public void  deleteNotification(){
        log.info("----[scheduler] 오래된 알림 정리 작업 시작 (30일 경과 기준) ---");

        int deleteCount = notificationMapper.deleteOldNotifications();

        log.info("---[scheduler] 정리 완료: 총 {}건의 알림이 삭제되었습니다. ---", deleteCount);
    }
}
