package org.yaroslaavl.communicationservice.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record VideoResponseDto(
        @NotNull UUID id,
        String recruiterId,
        String candidateId,
        LocalDateTime startedAt
) { }