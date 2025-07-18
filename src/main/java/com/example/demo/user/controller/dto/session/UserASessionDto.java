package com.example.demo.user.controller.dto.session;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserASessionDto implements Serializable {
    private Long id;
    private String userId;

    public UserASessionDto(Long id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }
} 