package com.example.demo.user.repository.userA;

import com.example.demo.user.entity.userA.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;



public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
}