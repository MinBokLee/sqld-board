package com.sqld_board.sqld.config.redis;

import com.sqld_board.sqld.service.redisConfig.RedisSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 설정을 담당한다.
 * 스프링 부트가 Redis 서버와 어떻게 통신할지, 데이터를 어떤 방식으로 저장할지 정의
 */
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.topic}")
    private String redisTopic;

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory
                                                       ,MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // "realtime" 이라는 이름의 토픽을 구독하도록 설정
        // 필요에 따라 여러 토픽을 등록할 수 도 있다.
        container.addMessageListener(listenerAdapter, new ChannelTopic(redisTopic));
        return container;
    }

    /**
     * 실제 메시지를 처리한느 비즈니스 로직(RedisSubscriber)을 연결해 준다.
     * "onMessages"는 나중에 만들 Subscriber 클래스의 메서드 이름이다.
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }


    // Redis 조회수 저장
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
/**
 * java.lang.ClassCastException 이 발생하는 이유.
 * new StringRedisSerializer() 오직 String 타입만 처리할 수 있다.
 * 그러나 WebSocketController 에서 RealTimeMessage 라는 Java 객체를 그대로 convertAndSend 로 던지니까 이를 처리할 수 없기에 에러가 발생.
 * 직접적으로 new StringRedisSerializer()를 new GenericJackson2JsonRedisSerializer를() 객체도 JSON으로 자동 변환해서 저장할 수 있도록
 * 설정을 바꿔야 한다.
 *
 * 그러나 이 경우, StringRedisSerializer를 사용하던 곳에서 GenericJackson2JsonRedisSerializer로 바꾸면,
 * 기존에 저장된 데이터의 형식(따옴표 유무 등)이 미세하게 달라져서 조회수 합산 로직에서 에러가 날 가능성이 있다.
 * 1. StringRedisSerializer: 숫자 100을 문자열 "100"으로 저장합니다. (Redis에서는 순수 숫자처럼 보임)
 * 2. GenericJackson2JsonRedisSerializer: JSON 형식을 유지하려고 하므로, 자바 타입 정보나 따옴표가 섞여서 저장될 수 있습니다. (예: 100 -> "100")
 * 3. 조회수 합산 로직: Long.parseLong() 등으로 데이터를 읽어올 때, 예상치 못한 따옴표나 문자열 형식이 들어오면 에러가 발생합니다.
 *
 * 그러기에 기존 조회수 로직은 건드리지 않고, WebSocket 컨트롤러에서만 데이터를 직접 JSON 문자열로 변환해서 던지는 것이 가장 안전하다.
 */
