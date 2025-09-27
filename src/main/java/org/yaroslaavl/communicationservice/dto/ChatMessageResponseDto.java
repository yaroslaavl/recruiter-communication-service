package org.yaroslaavl.communicationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.yaroslaavl.communicationservice.database.entity.enums.MessageStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageResponseDto(
        @NotNull UUID chatId,
        @NotBlank String senderId,
        @NotBlank String content,
        @NotNull MessageStatus status,
        @NotNull LocalDateTime sentAt
) { }
