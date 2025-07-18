package com.example.demo.aop.authCheck;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthAspect {

  private final HttpSession session;

  @Before("@annotation(com.example.demo.aop.authCheck.CheckUserA)")
  public void checkUserA() {
    if (session.getAttribute("userA") == null) {
      throw new RuntimeException("UserA 인증 필요");
    }
  }

  @Before("@annotation(com.example.demo.aop.authCheck.CheckUserB)")
  public void checkUserB() {
    if (session.getAttribute("userB") == null) {
      throw new RuntimeException("UserB 인증 필요");
    }
  }
}
