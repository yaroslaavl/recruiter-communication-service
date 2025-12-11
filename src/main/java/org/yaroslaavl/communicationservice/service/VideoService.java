package org.yaroslaavl.communicationservice.service;

import org.yaroslaavl.communicationservice.dto.VideoResponseDto;

import java.util.UUID;

public interface VideoService {

    void create(UUID applicationId, String candidateId, String pin);

    VideoResponseDto joinCall(String pin);

    boolean isRoomCreated(UUID applicationId);
}
