package com.sqld_board.sqld.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 *  인증은 되었으나(로그인 상태), 권한(ROLE_ADMIN등)이 없는 곳에 접근할 때 호출된다.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 권한이 없음을 알리는  430 Forbidden전송
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    }
}
