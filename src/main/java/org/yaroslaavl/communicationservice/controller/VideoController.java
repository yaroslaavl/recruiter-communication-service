package org.yaroslaavl.communicationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaroslaavl.communicationservice.dto.VideoResponseDto;
import org.yaroslaavl.communicationservice.service.VideoService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/call")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/create")
    public ResponseEntity<Void> createCall(@RequestParam("applicationId") UUID applicationId, @RequestParam("candidateId") String candidateId, @RequestParam("pin") String pin) {
        videoService.create(applicationId, candidateId, pin);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/join")
    public ResponseEntity<VideoResponseDto> joinCall(@RequestParam("pin") String pin) {
        return ResponseEntity.ok().body(videoService.joinCall(pin));
    }

    @GetMapping("/{applicationId}/exists")
    public ResponseEntity<Boolean> isRoomExists(@PathVariable("applicationId") UUID applicationId) {
        return ResponseEntity.ok().body(videoService.isRoomCreated(applicationId));
    }
}
