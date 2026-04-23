package com.sqld_board.sqld.config.webSocketConfig;

import com.sqld_board.sqld.exception.websocket.TokenSignatureException;
import com.sqld_board.sqld.handler.JwtHandler;
import com.sqld_board.sqld.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtHandler jwtHandler;

    private final MemberMapper memberMapper;

    @Value("${jwt.secret}")
    private String securityKey;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        //1. 연결 시도(CONNECT)일 때 토큰 검증
        if(StompCommand.CONNECT == accessor.getCommand()){
            String jwt = accessor.getFirstNativeHeader("Authorization");

            // [추가] 토큰이 아예 없는 경우 (추가된 부분)
            if(jwt == null || jwt.isEmpty()){
                throw new TokenSignatureException();
            }

            //Bearer "제거 로직 포함한 토큰 검증"
            if(jwt != null && jwt.startsWith("Bearer ")){
                jwt = jwt.substring(7);
            }

            // 토큰 검증 (실패 시 예외 발생)
            if(!jwtHandler.validateToken(securityKey,jwt)){
                throw new TokenSignatureException();
            }
            // 토큰 검증 성공 후, 사용자 정보 저장
            // 토큰에서 클레임(사용자 정보)를 추출
            jwtHandler.getClaims(securityKey, jwt).ifPresent(claims -> {
                String memberId = claims.getSubject();
                //
                accessor.setUser(() -> memberId);

                //[추가] 세션에 닉네임과 memberId 저장 (Disconnect 때 쓰기 위함)
                Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
                if(sessionAttributes != null){
                    // DB 조회를 한 후, 닉네임 setting
                    memberMapper.readMemberByMemberId(memberId).ifPresent(member ->{
                        sessionAttributes.put("senderName", member.getUserName());
                        sessionAttributes.put("senderId", memberId);
                    });
                }
            });
        }

        return message;
    } // end of preSend();
}
