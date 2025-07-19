package com.example.demo.user.controller;

import com.example.demo.aop.authResolver.userA.LoginUserA;
import com.example.demo.user.controller.dto.request.LoginARequest;
import com.example.demo.user.entity.userA.Department;
import com.example.demo.user.entity.userA.UserA;
import com.example.demo.user.repository.userA.UserARepository;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.demo.user.controller.dto.request.SignupARequest;
import com.example.demo.user.controller.dto.response.UserAInfoResponse;
import com.example.demo.user.service.UserAService;
import com.example.demo.user.controller.dto.session.UserASessionDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/a")
public class UserAController {

    private final UserAService userAService;
    private final UserARepository userARepository;

    @GetMapping("/search/department")
    public ResponseEntity<?> searchDepartment(@RequestParam String keyword) {
        List<UserAInfoResponse> userAInfoResponses = userAService.searchByDepartment(keyword);
        return ResponseEntity.ok(userAInfoResponses);
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(@LoginUserA UserASessionDto userA) {
        if (userA == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

 
        // 사용자 정보를 포함한 응답
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userA.getUserId());
        response.put("id", userA.getId());
        response.put("loginType", "a");
        
        return ResponseEntity.ok(response);    
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupARequest request) {
        try {
            userAService.signup(request);
            return ResponseEntity.ok("회원가입 완료");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginARequest request, HttpSession session) {
        Optional<UserA> userOpt = userARepository.findByUserId(request.getUserId());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("존재하지 않는 사용자입니다");
        }

        UserA user = userOpt.get();
        if (!user.checkPassword(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 불일치");
        }

        // UserASessionDto로 변환해서 세션에 저장
        UserASessionDto sessionDto = new UserASessionDto(user.getId(), user.getUserId());
        session.setAttribute("userA", sessionDto);
        return ResponseEntity.ok("로그인 성공");
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃 성공");
    }

}

