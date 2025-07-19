package com.example.demo.socket.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatMessage {
  public ChatMessage(String sender, String content, String senderType, LocalDateTime createdAt) {
    this.sender = sender;
    this.content = content;
    this.type = senderType; 
    this.createdAt = createdAt;
  }
  private String sender;  // 유저 ID
  private String content;
  private String type;    // "ENTER", "TALK", "LEAVE" 등으로 확장 가능
  private LocalDateTime createdAt;
}
