package com.example.demo.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.chat.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
