package com.example.demo.user.entity;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "users_b")
@ToString(exclude = "password")
@Getter
public class UserB implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String password;

    @OneToOne(mappedBy = "userB", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserBProfile profile;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
