package com.cochalla.cochalla.repository;

import com.cochalla.cochalla.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    List<Answer> findByQuestion_Chat_ChatId(Integer chatId);
}