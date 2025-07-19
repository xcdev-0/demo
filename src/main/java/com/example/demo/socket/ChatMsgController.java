package com.example.demo.socket;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.demo.chat.repository.MessagesRepository;
import com.example.demo.chat.entity.Messages;
import com.example.demo.socket.dto.ChatMessage;
import com.example.demo.socket.dto.WebSocketUserDto;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMsgController {

  private final MessagesRepository messagesRepository;

  @MessageMapping("/chat.send/{roomId}")
  @SendTo("/topic/chat/{roomId}")
  public ChatMessage sendMessage(@Payload ChatMessage message,
                                 @DestinationVariable Long roomId,
                                 SimpMessageHeaderAccessor headerAccessor) {
  
      WebSocketUserDto user = (WebSocketUserDto) headerAccessor.getSessionAttributes().get("user");
      log.info("메시지 보낸 유저: id={}, type={}", user.getId(), user.getUserType());
      Messages messages = Messages.builder()
        .senderId(user.getId())
        .senderUserId(user.getUserId())
        .content(message.getContent())
        .senderType(user.getUserType())
        .roomId(roomId)
        .createdAt(LocalDateTime.now())
        .build();
      messagesRepository.save(messages);
      // message.setSenderId(user.getId()); // 필요시 추가
      return message;
  }
  
}
