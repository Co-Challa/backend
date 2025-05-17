package com.cochalla.cochalla.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cochalla.cochalla.domain.User;

public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByNickname(String nickname);
}
