package com.example.demo.user.controller.dto.request;

import lombok.Data;

@Data
public class SignupBRequest {
    private String userId;
    private String password;
    private String information;
}
