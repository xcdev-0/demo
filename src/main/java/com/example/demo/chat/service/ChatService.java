package com.example.demo.chat.service;


import com.example.demo.chat.controller.dto.response.PendingChatListResponse;
import com.example.demo.chat.entity.ChatRequest;
import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.chat.entity.RequestStatus;
import com.example.demo.chat.repository.ChatRequestRepository;
import com.example.demo.chat.repository.ChatRoomRepository;
import com.example.demo.user.entity.UserB;
import com.example.demo.user.entity.UserBProfile;
import com.example.demo.user.repository.UserBRepository;
import com.example.demo.user.service.UserAService;
import com.example.demo.user.service.UserBService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

  private final ChatRequestRepository chatRequestRepository;
  private final UserAService userAService;
  private final UserBService userBService;
  private final UserBRepository userBRepository;
  private final ChatRoomRepository chatRoomRepository;
  @Transactional
  public void handleCall(Long aId, Long bId) {
    // null 체크 추가
    if (aId == null || bId == null) {
      throw new RuntimeException("aId 또는 bId가 null입니다. aId: " + aId + ", bId: " + bId);
    }
    
    if(userAService.findById(aId).isEmpty() || userBService.findById(bId).isEmpty()) {
      throw new RuntimeException("존재하지 않는 유저");
    }

    Optional<ChatRequest> existing = chatRequestRepository.findByAIdAndBIdAndStatus(aId, bId, RequestStatus.PENDING);
    if (existing.isPresent()) {
      // 이미 대기 중인 요청 있음 → 중복 방지
      log.info("이미 존재하는 PENDING 요청, 저장 안함");
      return;
    }

    // 새로 저장
    ChatRequest req = new ChatRequest();
    req.setAId(aId);
    req.setBId(bId);
    req.setStatus(RequestStatus.PENDING);
    chatRequestRepository.save(req);

    log.info("새 요청 저장 완료");
  }

  @Transactional
  public Long acceptChatRequest(Long aId, Long chatRequestId) {
    ChatRequest request = chatRequestRepository.findById(chatRequestId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 요청"));

    if(request.getAId() != aId) {
      throw new RuntimeException("권한 없음");
    }

    if(request.getStatus() != RequestStatus.PENDING) {
      throw new IllegalStateException("이미 처리된 요청입니다");
    }

    ChatRoom room = new ChatRoom();
    room.setAId(request.getAId());
    room.setBId(request.getBId());
    chatRoomRepository.save(room);

    request.setStatus(RequestStatus.ACCEPTED);
    chatRequestRepository.save(request);

    return room.getId();
  }

  @Transactional(readOnly = true)
  public List<PendingChatListResponse> getPendingRequests(Long aId) {
    log.info("getPendingRequests 호출됨 - aId: {}", aId);
    
    List<ChatRequest> requests = chatRequestRepository.findByAIdAndStatus(aId, RequestStatus.PENDING);
    log.info("조회된 PENDING 요청 개수: {}", requests.size());
    
    if (requests.isEmpty()) {
      log.info("PENDING 요청이 없습니다.");
      return List.of();
    }

    List<Long> bIds = requests.stream()
        .map(ChatRequest::getBId)
        .collect(Collectors.toList());
    log.info("조회된 bIds: {}", bIds);

    List<UserB> users = userBRepository.findAllWithProfileByIdIn(bIds);
    log.info("조회된 UserB 개수: {}", users.size());

    // BId → UserB 매핑
    Map<Long, UserB> userBMap = users.stream()
        .collect(Collectors.toMap(UserB::getId, Function.identity()));

    return requests.stream()
        .map(req -> {
            UserB b = userBMap.get(req.getBId());
            UserBProfile p = b.getProfile();
            return new PendingChatListResponse(req.getId(), b.getId(), p.getInformation());
        })
        .collect(Collectors.toList());
  }

}
