package com.cochalla.cochalla.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cochalla.cochalla.domain.Summary;

public interface SummaryRepository extends JpaRepository<Summary, Long> {

  boolean existsByChat_ChatId(Integer chatId);

  List<Summary> findAllByChat_User_UserId(String userId);

}
