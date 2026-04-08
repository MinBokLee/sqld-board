package com.sqld_board.sqld.common.scheduler;

import com.sqld_board.sqld.mapper.BoardMapper;
import com.sqld_board.sqld.service.board.ViewCountRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Redis에 저장된 조회수를 주기적으로 DB에 반영하는 Scheduler
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BoardViewCountScheduler {

    private final ViewCountRedisService redisService;
    private final BoardMapper boardMapper;

    /**
     * Redis에 저장된 조회수를 주기적으로 DB에 반영
     * cron 설정: 초 분 시 일 월 요일 (현재 설정은 10분마다 실행)
     */
    //@Scheduled(fixedDelay = 1000) 테스트 설정
    @Scheduled(cron = "0 0/5 * * * *")
    @Transactional
    public void updateViewCount() {
        log.info(">>> Redis 조회수 DB 동기화 시작");

        //1. Reais에서 "viewCount:board:*" 패턴의 모든 키를 가져온다.
        Set<String> keys = redisService.getAllViewCountKeys();

        if(keys == null || keys.isEmpty()){
            log.info(">>> 동기화할 조회수 데이터가 없습니다.");
            return;
        }

        for(String key : keys){
            //2. 키 이름에서 boardId 추출 (예: "viewCount:board:123" -> 123)
            Long boardId = Long.parseLong(key.split(":")[2]);

            //3. 해당 키의 현재 값(조회수)를 가져온다.
            int viewCount  = redisService.getViewCount(boardId);

            if(viewCount > 0){
                //4. DB에  한꺼번에 더한다.
                boardMapper.updateViewCountBulk(boardId, viewCount);

                //5. DB 반영이 성공했다면 Redis에서 해당 데이터를 삭제한다.
                //(삭제하지 않으면 다음 스케줄링 때 또, 더해지게 된다.
                redisService.deleteViewCount(key);
            }
        }//end or for()
        log.info(">>> Redias 조회수 DB 동기화 완료 (처리 건수: {})", keys.size());
    }
}
