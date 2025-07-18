package com.example.demo.socket.dto;

import lombok.Data;

@Data
public class ChatMessage {
  private Long roomId;
  private Long senderId;
  private String sender;  // 유저 ID
  private String content;
  private String type;    // "ENTER", "TALK", "LEAVE" 등으로 확장 가능
}
