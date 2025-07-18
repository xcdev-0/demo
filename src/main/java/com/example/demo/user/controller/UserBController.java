package com.example.demo.user.controller;

import com.example.demo.user.controller.dto.request.LoginBRequest;
import com.example.demo.user.controller.dto.request.SignupBRequest;
import com.example.demo.user.controller.dto.session.UserBSessionDto;
import com.example.demo.user.entity.UserB;
import com.example.demo.user.repository.UserBRepository;
import com.example.demo.user.service.UserBService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/b")
public class UserBController {

    private final UserBService userBService;
    private final UserBRepository userBRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupBRequest request) {
        try {
            userBService.signup(request);
            return ResponseEntity.ok("회원가입 완료");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginBRequest request, HttpSession session) {
        Optional<UserB> userOpt = userBRepository.findByUserId(request.getUserId());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("존재하지 않는 사용자입니다");
        }

        UserB user = userOpt.get();
        if (!user.checkPassword(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 불일치");
        }

        UserBSessionDto sessionDto = new UserBSessionDto(user.getId(), user.getUserId());
        session.setAttribute("userB", sessionDto);
        return ResponseEntity.ok("로그인 성공");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃 성공");
    }
}
