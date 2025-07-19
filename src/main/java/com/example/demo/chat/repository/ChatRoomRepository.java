package com.example.demo.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.chat.entity.ChatRequest;
import com.example.demo.chat.entity.ChatRoom;
import com.example.demo.chat.entity.RequestStatus;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.aId = :aId")
    List<ChatRoom> findByAId(Long aId);

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.bId = :bId")
    List<ChatRoom> findByBId(Long bId);

}
