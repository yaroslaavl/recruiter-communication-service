package org.yaroslaavl.communicationservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.communicationservice.database.entity.Video;
import org.yaroslaavl.communicationservice.database.entity.enums.CallStatus;
import org.yaroslaavl.communicationservice.database.repository.VideoRepository;
import org.yaroslaavl.communicationservice.dto.VideoResponseDto;
import org.yaroslaavl.communicationservice.service.SecurityContextService;
import org.yaroslaavl.communicationservice.service.VideoService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.yaroslaavl.communicationservice.service.impl.SecurityContextServiceImpl.SUB;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final SecurityContextService securityContextService;
    private final VideoRepository videoRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Long VIDEO_PIN_LIFE_SPAN = 30L;

    @Override
    @Transactional
    public void create(UUID applicationId, String candidateId, String pin) {
        String authenticatedUserInfo = securityContextService.getAuthenticatedUserInfo(SUB);
        Video video = new Video();
        video.setApplicationId(applicationId);
        video.setRecruiterId(authenticatedUserInfo);
        video.setCandidateId(candidateId);
        video.setPin(passwordEncoder.encode(pin));
        video.setPinExpiresAt(LocalDateTime.now().plusMinutes(VIDEO_PIN_LIFE_SPAN));
        video.setRecruiterJoined(false);
        video.setCandidateJoined(false);
        video.setStatus(CallStatus.CREATED);

        videoRepository.save(video);
    }

    @Override
    @Transactional
    public VideoResponseDto joinCall(String pin) {
        log.info("Attempting to join with PIN: {}", pin);
        Video call = videoRepository.findByPin(pin)
                .orElseThrow(() -> new RuntimeException("Invalid PIN"));

        if (call.getPinExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("PIN expired");
        }

        String authenticatedUserInfo = securityContextService.getAuthenticatedUserInfo(SUB);

        if (authenticatedUserInfo.equals(call.getRecruiterId())) {
            call.setRecruiterJoined(true);
        } else if (authenticatedUserInfo.equals(call.getCandidateId())) {
            call.setCandidateJoined(true);
        } else {
            throw new RuntimeException("User not allowed in this call");
        }

        if (call.getRecruiterJoined() && call.getCandidateJoined()) {
            call.setStatus(CallStatus.ACTIVE);
        }

        videoRepository.save(call);

        return new VideoResponseDto(
                call.getId(),
                call.getRecruiterId(),
                call.getCandidateId(),
                LocalDateTime.now()
        );
    }

    @Override
    public boolean isRoomCreated(UUID applicationId) {
        return videoRepository.existsByApplicationId(applicationId);
    }
}
