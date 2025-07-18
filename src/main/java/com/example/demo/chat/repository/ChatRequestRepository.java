package com.example.demo.chat.repository;

import com.example.demo.chat.entity.ChatRequest;
import com.example.demo.chat.entity.RequestStatus;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.chat.entity.ChatRequest;
import com.example.demo.chat.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRequestRepository extends JpaRepository<ChatRequest, Long> {

  @Query("SELECT cr FROM ChatRequest cr WHERE cr.aId = :aId AND cr.bId = :bId AND cr.status = :status") 
  Optional<ChatRequest> findByAIdAndBIdAndStatus(@Param("aId") Long aId, @Param("bId") Long bId, @Param("status") RequestStatus status);

  @Query("SELECT cr FROM ChatRequest cr WHERE cr.aId = :aId")
  List<ChatRequest> findByAId(@Param("aId") Long aId);

  @Query("SELECT cr FROM ChatRequest cr WHERE cr.bId = :bId")
  List<ChatRequest> findByBId(@Param("bId") Long bId);

  // 특정 상태로 필터링해서 조회도 가능
  @Query("SELECT cr FROM ChatRequest cr WHERE cr.aId = :aId AND cr.status = :status")
  List<ChatRequest> findByAIdAndStatus(@Param("aId") Long aId, @Param("status") RequestStatus status);

  @Query("SELECT cr FROM ChatRequest cr WHERE cr.bId = :bId AND cr.status = :status")
  List<ChatRequest> findByBIdAndStatus(@Param("bId") Long bId, @Param("status") RequestStatus status);
}
