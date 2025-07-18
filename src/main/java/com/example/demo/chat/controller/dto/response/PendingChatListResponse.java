package com.example.demo.chat.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PendingChatListResponse {
    private Long id;
    private Long bId;
    private String information;
}
