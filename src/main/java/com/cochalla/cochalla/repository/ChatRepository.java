package com.cochalla.cochalla.repository;

import com.cochalla.cochalla.domain.Chat;
import com.cochalla.cochalla.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

  Optional<Chat> findTopByUser_UserIdOrderByCreatedAtDesc(String userId);

  List<Chat> findAllByUser(User user);

}
