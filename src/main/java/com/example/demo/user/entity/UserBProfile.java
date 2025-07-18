package com.example.demo.user.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
public class UserBProfile implements Serializable {
  @Id
  private Long userBId;

  @OneToOne
  @MapsId  // userBId = userB.id
  @JoinColumn(name = "user_b_id")
  private UserB userB;

  private String information;
}
