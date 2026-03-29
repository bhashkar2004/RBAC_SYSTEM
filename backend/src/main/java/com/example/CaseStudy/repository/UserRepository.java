package com.example.CaseStudy.repository;

import com.example.CaseStudy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT u FROM User u LEFT JOIN FETCH u.roles")
    java.util.List<User> findAllWithRoles();

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserEmail(String userEmail);

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserName(String userName);
}
