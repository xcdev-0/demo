package com.example.demo.user.repository.userA;

import com.example.demo.user.entity.userA.UserA;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserARepository extends JpaRepository<UserA, Long> {

    boolean existsByUserId(String userId);

    Optional<UserA> findByUserId(String userId);

    @Query("SELECT distinct ua FROM UserA ua " +
           "JOIN FETCH ua.departments ud " +
           "JOIN FETCH ud.department d " +
           "WHERE d.name LIKE %:keyword%")
    List<UserA> findByDepartmentsNameContaining(@Param("keyword") String keyword);
}

