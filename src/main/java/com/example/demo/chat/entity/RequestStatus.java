package com.example.demo.chat.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum RequestStatus {
  PENDING, ACCEPTED, REJECTED
}
