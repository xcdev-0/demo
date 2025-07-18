package com.example.demo.user.repository;

import com.example.demo.user.entity.UserB;
import com.example.demo.user.entity.UserBProfile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserBProfileRepository extends JpaRepository<UserBProfile, Long> {
    
    // 예: 유저 ID로 프로필 조회
    UserBProfile findByUserBId(Long userBId);
    
}
