package com.example.demo.user.controller.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class SignupARequest {
    private String userId;
    private String password;
    private List<String> departments;
}
