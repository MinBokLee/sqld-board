package com.sqld_board.sqld.service.board;

import com.sqld_board.sqld.mapper.AdminMapper;
import com.sqld_board.sqld.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardBatchService {

    private final AdminMapper adminMapper;

    // 매일 새벽 3시에 실행(초, 분, 시, 일, 월, 요일
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupOldBoard(){
        log.info("[배치 작업] 오래된 게시물 물리 삭제 시작 ---!");
        int deletedCount = adminMapper.deleteOldSoftDeletedBoards();
        log.info("[배치 작업] 완료: 총 {}건의 데이터가 영구 삭제되었습니다.", deletedCount);
    }

}
