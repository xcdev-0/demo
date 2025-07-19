package com.example.demo.chat.controller;

import com.example.demo.aop.authCheck.CheckUserA;
import com.example.demo.aop.authCheck.CheckUserB;
import com.example.demo.aop.authResolver.userA.LoginUserA;
import com.example.demo.aop.authResolver.userB.LoginUserB;
import com.example.demo.chat.controller.dto.CallRequest;
import com.example.demo.chat.controller.dto.response.AcceptChatRequest;
import com.example.demo.chat.controller.dto.response.ActiveChatListResponse;
import com.example.demo.chat.controller.dto.response.PendingChatListResponse;
import com.example.demo.chat.service.ChatService;
import com.example.demo.user.controller.dto.session.UserASessionDto;
import com.example.demo.user.controller.dto.session.UserBSessionDto;
import com.example.demo.user.entity.UserB;
import com.example.demo.user.entity.userA.UserA;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;

  @GetMapping("/history")
  public ResponseEntity<?> getChatHistory(
      @RequestParam Long roomId,
      @LoginUserA UserASessionDto userA,
      @LoginUserB UserBSessionDto userB
  ) {
    if (userA != null) {
      return ResponseEntity.ok(chatService.getChatHistory(roomId, userA.getId()));
    } else if (userB != null) {
      return ResponseEntity.ok(chatService.getChatHistory(roomId, userB.getId()));
    } else {
      return ResponseEntity.badRequest().body("로그인 필요");
    }
  }
  
  @CheckUserB
  @PostMapping("/call")
  public ResponseEntity<?> callNext(
      @RequestBody CallRequest request,
      @LoginUserB UserBSessionDto userB) {
    chatService.handleCall(request.getAId(), userB.getId());
    return ResponseEntity.ok("호출 요청 완료");
  }

  @CheckUserA
  @GetMapping("/pending")
  public ResponseEntity<?> getPendingRequests(HttpSession session, @LoginUserA UserASessionDto userA) {
    List<PendingChatListResponse> list = chatService.getPendingRequests(userA.getId());
    return ResponseEntity.ok(list);
  }

  @CheckUserA
  @GetMapping("/a/active")
  public ResponseEntity<?> getActiveRequests(HttpSession session, @LoginUserA UserASessionDto userA) {
    List<ActiveChatListResponse> list = chatService.getAUserActiveRequests(userA.getId());
    return ResponseEntity.ok(list);
  }

  @CheckUserB
  @GetMapping("/b/active")
  public ResponseEntity<?> getBUserActiveRequests(HttpSession session, @LoginUserB UserBSessionDto userB) {
    List<ActiveChatListResponse> list = chatService.getBUserActiveRequests(userB.getId());
    return ResponseEntity.ok(list);
  }

  @CheckUserA
  @PostMapping("/accept")
  public ResponseEntity<?> acceptChat(
      @LoginUserA UserASessionDto userA,
      @RequestBody AcceptChatRequest request
  ) {
    Long roomId = chatService.acceptChatRequest(userA.getId(), request.getChatRequestId());
    return ResponseEntity.ok(roomId);
  }


}
