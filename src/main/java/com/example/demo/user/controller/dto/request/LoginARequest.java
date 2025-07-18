package com.example.demo.user.controller.dto.request;

import lombok.Data;

@Data
public class LoginARequest {
  private String userId;
  private String password;
}
