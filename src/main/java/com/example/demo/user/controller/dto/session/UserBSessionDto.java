package com.example.demo.user.controller.dto.session;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserBSessionDto implements Serializable {
    private Long id;
    private String userId;

    public UserBSessionDto(Long id, String userId) {
        this.id = id;
        this.userId = userId;
    }
} 