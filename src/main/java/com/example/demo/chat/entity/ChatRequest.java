package com.example.demo.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChatRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @Column(name = "a_id")
  private Long aId;
  
  @Column(name = "b_id")
  private Long bId;
  
  @Enumerated(EnumType.STRING)
  public RequestStatus status = RequestStatus.PENDING;



}
