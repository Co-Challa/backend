package com.cochalla.cochalla.repository;

import com.cochalla.cochalla.domain.Chat;
import com.cochalla.cochalla.domain.Summary;
import com.cochalla.cochalla.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SummaryRepository extends JpaRepository<Summary, Integer> {
    Optional<Summary> findTopByUser_UserIdOrderByCreatedAtDesc(String userId);

    boolean existsByUserAndChat(User user, Chat chat);

    Optional<Summary> findByChat(Chat chat);
}
