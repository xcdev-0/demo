package com.example.demo.user.controller.dto.request;

import lombok.Data;

@Data
public class LoginBRequest {
  private String userId;
  private String password;
}
