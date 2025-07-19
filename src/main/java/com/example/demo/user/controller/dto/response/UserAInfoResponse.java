package com.example.demo.user.controller.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.user.entity.userA.Department;
import com.example.demo.user.entity.userA.UserADepartment;

import lombok.Data;

@Data
public class UserAInfoResponse {
    public UserAInfoResponse(Long id, String userId, List<UserADepartment> departments) {
        this.id = id;
        this.userId = userId;
        this.department = departments.stream()
            .map(UserADepartment::getDepartment)
            .map(Department::getName)
            .collect(Collectors.toList());
    }
    private Long id;
    private String userId;
    private List<String> department;
}
