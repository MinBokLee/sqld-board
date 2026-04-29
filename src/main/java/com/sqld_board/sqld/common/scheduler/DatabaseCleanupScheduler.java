package com.sqld_board.sqld.common.scheduler;

import com.sqld_board.sqld.mapper.AdminMapper;
import com.sqld_board.sqld.mapper.WebSocketMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 시스템 스케줄러 게시판 및 데이터베이스 최적화 관리
 *
 *  역활:
 * 1. 서비스 성능 유지 및 스토리지 용량 확보를 위해 불필요한 데이터를 주기적으로 정리
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseCleanupScheduler {

    private final AdminMapper adminMapper;

    private final WebSocketMapper webSocketMapper;



    /**
     * 논리적으로 삭제된 "DELETE_YN=Y" 데이터중 30일이 지난 데이터를 물리적으로 삭제
     */
    @Scheduled(cron = "0 0 3 * * *") //매일 새벽 3시에 실행(초, 분, 시, 일, 월, 요일)
    public void cleanupOldBoard(){
        log.info("[cleanupOldBoard scheduler] 오래된 게시물 물리 삭제 시작 ---!");

        try {
            int deletedCount = adminMapper.deleteOldSoftDeletedBoards(30);

            log.info("[scheduler] 완료: 총 {}건의 데이터가 영구 삭제되었습니다.", deletedCount);
        } catch (Exception e) {
            log.error("[cleanupOldBoard scheduler] 게시판 정리 중 오류 발생 : {}" ,e.getMessage(), e);
        }
    }

    /**
     * 채팅 로그 정리 (새벽 3시 30분) 진행 (7일이 지난 채팅 메시지 정리)
     */
    @Scheduled(cron ="0 30 3 * * *")
    public void cleanupOldMessage(){
        log.info("[cleanupOldMessage Scheduler] 7일이 지난 채팅 메시지 삭제 시작...");

        try {
            int deleteCount = webSocketMapper.deleteOldChatMessage(7);

            log.info("[Scheduler] 완료: {}건의 채팅 기록이 삭제되었습니다.", deleteCount);
        } catch (Exception e) {
            log.error("[cleanupOldMessage Scheduler] 채팅 메시지 정리 중 오류 발생 : {} ", e.getMessage(),e);
        }
    }


}