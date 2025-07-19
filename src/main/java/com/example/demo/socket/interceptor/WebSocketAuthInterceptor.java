package com.example.demo.socket.interceptor;

import com.example.demo.user.controller.dto.session.UserASessionDto;
import com.example.demo.user.controller.dto.session.UserBSessionDto;
import com.example.demo.socket.dto.WebSocketUserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();
            HttpSession session = httpRequest.getSession(false);
            log.info("☘️ websocket auth interceptor: " + session);
            if (session != null) {
                Object userA = session.getAttribute("userA");
                Object userB = session.getAttribute("userB");

                if (userA instanceof UserASessionDto aDto) {
                    WebSocketUserDto dto = new WebSocketUserDto(aDto.getId(), aDto.getUserId(), "A");
                    attributes.put("user", dto);
                    log.info("[WebSocket 인증] A 사용자 인증됨: {}", dto);
                    return true;
                }

                if (userB instanceof UserBSessionDto b) {
                    WebSocketUserDto dto = new WebSocketUserDto(b.getId(), b.getUserId(), "B");
                    attributes.put("user", dto);
                    log.info("[WebSocket 인증] B 사용자 인증됨: {}", dto);
                    return true;
                }
            }
        }

        log.warn("[WebSocket 인증 실패] 세션 정보 없음");
        return false; // 인증 실패 시 WebSocket 연결 거부
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // 필요 시 로그 등 후처리
    }
}
