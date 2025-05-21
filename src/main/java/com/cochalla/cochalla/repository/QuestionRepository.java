package com.cochalla.cochalla.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cochalla.cochalla.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

  // List<Question> findWithAnswersByChatIdAndUserId(Integer chatId, String
  // userId);
  @Query("""
          SELECT q FROM Question q
          LEFT JOIN FETCH q.answer
          WHERE q.chat.chatId = :chatId AND q.chat.user.userId = :userId
          ORDER BY q.createdAt ASC
      """)
  List<Question> findWithAnswersByChatIdAndUserId(@Param("chatId") Integer chatId, @Param("userId") String userId);

  List<Question> findTop3ByChat_ChatIdOrderByCreatedAtDesc(Integer chatId);

}
