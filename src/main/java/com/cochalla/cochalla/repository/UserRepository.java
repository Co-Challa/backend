package com.cochalla.cochalla.repository;

import com.cochalla.cochalla.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}