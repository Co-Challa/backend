package com.cochalla.cochalla.service;

import com.cochalla.cochalla.domain.Answer;
import com.cochalla.cochalla.domain.Chat;
import com.cochalla.cochalla.domain.Question;
import com.cochalla.cochalla.dto.QuestionAnswerPairDto;
import com.cochalla.cochalla.repository.AnswerRepository;
import com.cochalla.cochalla.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QaService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public List<QuestionAnswerPairDto> getQAPairs(Chat chat) {
        List<Question> questions = questionRepository.findByChat_ChatId(chat.getChatId());
        List<Answer> answers = answerRepository.findByQuestion_Chat_ChatId(chat.getChatId());

        Map<Integer, Answer> answerMap = answers.stream().collect(Collectors.toMap(a -> a.getQuestion().getQuestionId(), a -> a));

        questions.sort(Comparator.comparing(Question::getCreatedAt));

        List<QuestionAnswerPairDto> result = new ArrayList<>();
        for (Question question : questions) {
            Answer answer = answerMap.get(question.getQuestionId());
            if (answer != null) {
                result.add(new QuestionAnswerPairDto(question.getQuestion(), answer.getAnswer()));
            }
        }

        return result;
    }
}
