package com.example.demo.user.service;

import com.example.demo.user.entity.userA.Department;
import com.example.demo.user.entity.userA.UserA;
import com.example.demo.user.entity.userA.UserADepartment;
import com.example.demo.user.repository.userA.UserADepartmentRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import com.example.demo.chat.entity.ChatRequest;
import com.example.demo.user.controller.dto.request.SignupARequest;
import com.example.demo.user.controller.dto.response.UserAInfoResponse;
import com.example.demo.user.repository.userA.DepartmentRepository;
import com.example.demo.user.repository.userA.UserARepository;

@Service
@RequiredArgsConstructor
public class UserAService {

    private final UserARepository userARepository;
    private final DepartmentRepository departmentRepository;
    private final UserADepartmentRepository userADepartmentRepository;

    @Transactional
    public void signup(SignupARequest request) {
        if (userARepository.existsByUserId(request.getUserId())) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }

        UserA userA = new UserA();
        userA.setUserId(request.getUserId());
        userA.setPassword(request.getPassword()); // TODO: 나중에 암호화

        List<UserADepartment> deptLinks = request.getDepartments().stream().map(deptName -> {
            Department dept = departmentRepository.findByName(deptName)
                .orElseGet(() -> {
                    Department newDept = new Department();
                    newDept.setName(deptName);
                    return departmentRepository.save(newDept);
                });

            UserADepartment link = new UserADepartment();
            link.setUserA(userA);
            link.setDepartment(dept);
            return link;
        }).collect(Collectors.toList());

        userA.setDepartments(deptLinks);

        userARepository.save(userA);
        userADepartmentRepository.saveAll(deptLinks);
    }

    public Optional<UserA> findById(Long aId) {
        return userARepository.findById(aId);
    }

    public List<UserAInfoResponse> searchByDepartment(String keyword) {
        List<UserA> users = userARepository.findByDepartmentsNameContaining(keyword);
        return users.stream()
            .map(user -> new UserAInfoResponse(user.getId(), user.getUserId(), user.getDepartments()))
            .collect(Collectors.toList());
    }
}
