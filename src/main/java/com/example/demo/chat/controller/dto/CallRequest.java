package com.example.demo.chat.controller.dto;


import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CallRequest {
  @JsonProperty("aId")
  private Long aId;
}
