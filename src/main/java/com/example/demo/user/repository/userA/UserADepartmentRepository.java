package com.example.demo.user.repository.userA;

import com.example.demo.user.entity.userA.UserA;
import com.example.demo.user.entity.userA.UserADepartment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserADepartmentRepository extends JpaRepository<UserADepartment, Long> {

    @Query("SELECT uad FROM UserADepartment uad " +
           "JOIN FETCH uad.department d " +
           "JOIN FETCH uad.userA ua " +
           "WHERE d.name LIKE %:keyword%")
    List<UserADepartment> findByDepartmentNameContaining(@Param("keyword") String keyword);
}


