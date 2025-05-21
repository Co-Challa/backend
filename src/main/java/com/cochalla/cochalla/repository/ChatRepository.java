package com.cochalla.cochalla.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cochalla.cochalla.domain.Chat;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

  Optional<Chat> findTopByUser_UserIdOrderByCreatedAtDesc(String userId);

}
