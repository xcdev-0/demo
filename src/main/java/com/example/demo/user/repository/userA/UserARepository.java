package com.example.demo.user.repository.userA;

import com.example.demo.user.entity.userA.UserA;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserARepository extends JpaRepository<UserA, Long> {

    boolean existsByUserId(String userId);

    Optional<UserA> findByUserId(String userId);
}

