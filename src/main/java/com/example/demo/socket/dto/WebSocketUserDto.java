package com.example.demo.socket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebSocketUserDto {
    private Long id;           // userA.id 또는 userB.id
    private String userId;     // userId
    private String userType;   // "A" or "B"
}
