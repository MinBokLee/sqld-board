package com.sqld_board.sqld.config.webSocketConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // WebSocket 메시지 브로커 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    private final StompHandler stompHandler;

    public WebSocketConfig(StompHandler stompHandler) {
        this.stompHandler = stompHandler;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        /**
         * STOMP 프로토콜 사용: 순수 WebSocket보다 메시징 규격이 정의된 STOMP(Simple Text Oriented Messaging Protocol)을
         * 사용하는 것이 관리가 쉬움(Redis /pub으로 보내고 /sub으로 받기)
         */

        // 메시지 구독 요청의 접두사 (메시지를 받을 때)
        config.enableSimpleBroker("/sub");

        // 메시지 발행 요청의 접두사 (메시지 보낼 때)
        config.setApplicationDestinationPrefixes("/pub");
    } //end of configureMessageBroker();

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //연결할 소켓 엔트포인트 지정
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*") // 모든 도메인 허용 (null, 파일 실행 포함)
                .withSockJS(); //SockJs 지원
    }
}//end Of WebSocketConfig()

