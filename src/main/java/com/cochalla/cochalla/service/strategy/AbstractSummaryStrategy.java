package com.cochalla.cochalla.service.strategy;

import com.cochalla.cochalla.domain.Chat;
import com.cochalla.cochalla.domain.Summary;
import com.cochalla.cochalla.dto.GptSummaryResponseDto;
import com.cochalla.cochalla.repository.SummaryRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractSummaryStrategy implements SummaryStrategy{

    protected final SummaryRepository summaryRepository;

    protected void saveSummary(Chat chat, GptSummaryResponseDto response) {
        Optional<Summary> optional = summaryRepository.findByChat(chat);

        Summary summary = optional.orElseGet(() ->
                Summary.of(chat.getUser(), chat, response.getTitle(), response.getContent())
        );

        if (optional.isPresent()) {
            summary.updateFromResponse(response);
        }

        summaryRepository.save(summary);
    }
}
