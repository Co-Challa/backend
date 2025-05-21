package com.cochalla.cochalla.repository;

import com.cochalla.cochalla.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    List<Question> findByChat_ChatIdOrderByCreatedAtAsc(Integer chatId);

    List<Question> findByChat_ChatId(Integer chatId);
}
