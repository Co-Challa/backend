package com.cochalla.cochalla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cochalla.cochalla.domain.User;

public interface UserRepository extends JpaRepository<User, String> {
    
}
