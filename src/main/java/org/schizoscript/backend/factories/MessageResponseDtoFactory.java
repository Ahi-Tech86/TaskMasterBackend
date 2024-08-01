package org.schizoscript.backend.factories;

import org.schizoscript.backend.dtos.MessageResponseDto;
import org.springframework.stereotype.Component;

@Component
public class MessageResponseDtoFactory {

    public MessageResponseDto makeMessageResponseDto(String message) {

        return MessageResponseDto
                .builder()
                .answer(message)
                .build();
    }
}
