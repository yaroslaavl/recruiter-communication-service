package org.yaroslaavl.communicationservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class VideoWsController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/video/{roomId}/offer")
    public void handleOffer(@DestinationVariable String roomId, @Payload Map<String, Object> payload) {
        log.info("Offer in room: {}", roomId);
        messagingTemplate.convertAndSend("/topic/video/" + roomId + "/offer", payload);
    }

    @MessageMapping("/video/{roomId}/answer")
    public void handleAnswer(@DestinationVariable String roomId, @Payload Map<String, Object> payload) {
        log.info("Answer in room: {}", roomId);
        messagingTemplate.convertAndSend("/topic/video/" + roomId + "/answer", payload);
    }

    @MessageMapping("/video/{roomId}/candidate")
    public void handleCandidate(@DestinationVariable String roomId, @Payload Map<String, Object> payload) {
        log.info("Candidate in room: {}", roomId);
        messagingTemplate.convertAndSend("/topic/video/" + roomId + "/candidate", payload);
    }

    @MessageMapping("/video/{roomId}/join")
    public void handleJoin(@DestinationVariable String roomId, @Payload Map<String, Object> payload) {
        log.info("User joined room: {}", roomId);
        messagingTemplate.convertAndSend("/topic/video/" + roomId + "/join", payload);
    }
}