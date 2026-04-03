package com.sqld_board.sqld.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 설정을 담당한다.
 * 스프링 부트가 Redis 서버와 어떻게 통신할지, 데이터를 어떤 방식으로 저장할지 정의
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        //1. Redis 서버와의 연결 통로를 설정
        template.setConnectionFactory(redisConnectionFactory);

        //2. Key와 value를 모두 String 직렬화하여 저장 ( 사람이 읽을 수 있는 String으로 변환)
        //이 설정이 없는 경우, redis-cli에서 키 이름이 \xzc\zad처럼 깨져보인다.
        template.setKeySerializer(new StringRedisSerializer());

        //3. Redis의 'Value(값)'를 저장하는 방식 설정
        // 조회수 숫자를 저장하거나 문자열을 저장할 때 깨지지 않도록 설정한다.
        template.setValueSerializer(new StringRedisSerializer());

        //4. Hash 구조(Key-Field-Value)를 사용할 경우를 대비한 설정
        // 에시: '게시글 1' 이라는 키안에 '조회수', '추천수'등을 묶어서 저장할 때 사용
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        return template;
    }
}
