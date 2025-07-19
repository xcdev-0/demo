package com.example.demo.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.example.demo.chat.entity.Messages;

public interface MessagesRepository extends JpaRepository<Messages, Long> {
    List<Messages> findByRoomIdOrderByCreatedAtAsc(Long roomId);
}
