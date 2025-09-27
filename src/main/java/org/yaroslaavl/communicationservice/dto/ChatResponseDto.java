package org.yaroslaavl.communicationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.yaroslaavl.communicationservice.database.entity.enums.MessageStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatResponseDto(
        @NotNull UUID id,
        @NotNull UUID applicationId,
        String companyLogoUrl,
        @NotBlank String vacancyTitle,
        @NotBlank String lastMessage,
        @NotNull Boolean isForCurrentUser,
        @NotNull MessageStatus lastMessageStatus,
        @NotNull LocalDateTime lastMessageDate
) { }
