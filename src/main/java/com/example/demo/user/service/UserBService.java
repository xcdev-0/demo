package com.example.demo.user.service;

import com.example.demo.chat.entity.ChatRequest;
import com.example.demo.user.controller.dto.request.SignupBRequest;
import com.example.demo.user.entity.UserB;
import com.example.demo.user.entity.UserBProfile;
import com.example.demo.user.repository.UserBRepository;
import com.example.demo.user.repository.UserBProfileRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserBService {

    private final UserBRepository userBRepository;
    private final UserBProfileRepository userBProfileRepository;

    @Transactional
    public void signup(SignupBRequest request) {
        if (userBRepository.existsByUserId(request.getUserId())) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }
    
        UserB userB = new UserB();
        userB.setUserId(request.getUserId());
        userB.setPassword(request.getPassword());
    
        // 1️⃣ 먼저 userB 저장 → ID 생성됨
        userBRepository.save(userB);
    
        // 2️⃣ 그 후에 프로필 생성 + 연결
        UserBProfile profile = new UserBProfile();
        profile.setUserB(userB); // @MapsId 로 userB.id 가 PK + FK 됨
        profile.setInformation(request.getInformation());
    
        userBProfileRepository.save(profile); // 이제 저장 가능!
    }
    
    public Optional<UserB> findById(Long bId) {
        return userBRepository.findById(bId);
    }
}

