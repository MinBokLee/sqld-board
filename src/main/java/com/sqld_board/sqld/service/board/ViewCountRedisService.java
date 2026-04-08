package com.sqld_board.sqld.service.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ViewCountRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
        ;
    // Redis에 저장할 키의 공통 이름(namespace)
    // Redis는 모든 데이터를 '키((key)'로 관리한다.
    // 단순히 1이라고 저장하면 이게 게시글 번호인지 , 회원 번호인지 알 수 없으므로, viewCount:board:1처럼 앞에이름을(namespace) 붙여서 관리한다.
    private static final String VIEW_COUNT_KEY_PREFIX = "viewCount:board:";

    /**
     * 게시글의 조회수를 1증가 (Redisd의 INCR 명령 사용)
     * @param boardId
     */
    public void incrViewCount(Long boardId) {
        String key = VIEW_COUNT_KEY_PREFIX + boardId;

        //Redis의 'increment' 메서드는 원자적(Atomic)으로 동작
        // 여러 자용자가 동시에 클릭해도 숫자가 정확하게 1씩 올라감.
        redisTemplate.opsForValue().increment(key);
    }

    /**
     * 특정 게시글의 현재 Redis에 쌓인 조회수를 가져옴
     * @param boardId
     * @return
     */
    public int getViewCount(Long boardId) {
        String key = VIEW_COUNT_KEY_PREFIX + boardId;
        String val = (String) redisTemplate.opsForValue().get(key);

        return val == null ? 0 : Integer.parseInt(val);
    }

    /**
     * Redis에 저장된 모든 조회수 관련(Key)들을 가져온다 (추 후 스케줄러에서 사용)
     * @return
     */
    public Set<String> getAllViewCountKeys(){
        return redisTemplate.keys(VIEW_COUNT_KEY_PREFIX + "*");
    }

    /**
     * DB에 반영이 완료된 후, Redis에서 해당 데이터를 삭제한다.
     * @param key
     */
    public void deleteViewCount(String key){
        redisTemplate.delete(key);
    }
}
