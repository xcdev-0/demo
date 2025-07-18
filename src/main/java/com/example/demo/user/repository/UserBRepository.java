package com.example.demo.user.repository;

import com.example.demo.user.entity.UserB;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserBRepository extends JpaRepository<UserB, Long> {
    Optional<UserB> findByUserId(String userId);
    boolean existsByUserId(String userId);
    @Query("SELECT b FROM UserB b JOIN FETCH b.profile WHERE b.id IN :ids")
    List<UserB> findAllWithProfileByIdIn(@Param("ids") List<Long> ids);
}

