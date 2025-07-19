package com.example.demo.socket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.example.demo.socket.interceptor.WebSocketAuthInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final WebSocketAuthInterceptor webSocketAuthInterceptor;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    // 클라인트가 받는 경로
    config.enableSimpleBroker("/topic");  
    // 서버가 받는경로
    config.setApplicationDestinationPrefixes("/app"); 
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws-chat")
            .addInterceptors(webSocketAuthInterceptor) // 👈 인터셉터 등록
            .setAllowedOriginPatterns("*");
            // .withSockJS();  // ← 이 부분 주석처리!
  }
}

