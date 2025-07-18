package com.example.demo.user.entity.userA;

import com.example.demo.user.entity.userA.Department;
import com.example.demo.user.entity.userA.UserA;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Setter;

@Entity
@Setter
public class UserADepartment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_a_id")
    private UserA userA;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
