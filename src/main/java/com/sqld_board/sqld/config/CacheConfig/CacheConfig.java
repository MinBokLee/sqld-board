package com.sqld_board.sqld.config.CacheConfig;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * DB 부하 감소 및 응답 속도 향상
     * 인기 게시글  조회 최적화
     * 조회한 최신 데이터를 10분동안 캐싱해두면 10분 동안은 DB 호출 없이 메모리에서 즉시 데이터를 반환할 수 있다.
     * 10분이 지나면 캐시가 만료되어, DB에서 최신 정보를 다시 가져오므로 어느 정도의 데이터 실시간도 보장.
     * @return
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("popularPosts");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                                         .expireAfterWrite(10, TimeUnit.MINUTES)// 저장 후, 10분 뒤 만료
                                         .maximumSize(100)); // 최대 100개 까지 생성
        return cacheManager;
    }

}
