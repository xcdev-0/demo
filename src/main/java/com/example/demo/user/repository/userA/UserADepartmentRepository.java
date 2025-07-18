package com.example.demo.user.repository.userA;

import com.example.demo.user.entity.userA.UserA;
import com.example.demo.user.entity.userA.UserADepartment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserADepartmentRepository extends JpaRepository<UserADepartment, Long> {
}


