package com.example.demo.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class ChatRoom {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  private Long aId;
  private Long bId;

}
