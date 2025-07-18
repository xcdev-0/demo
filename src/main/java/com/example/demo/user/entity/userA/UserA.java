package com.example.demo.user.entity.userA;

import jakarta.persistence.GenerationType;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;


import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Setter
@Getter
@ToString(exclude = "password")
public class UserA implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String userId;
    private String password;

    @OneToMany(mappedBy = "userA", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserADepartment> departments = new ArrayList<>();

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
}
