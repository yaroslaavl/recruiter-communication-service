package org.yaroslaavl.communicationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.yaroslaavl.communicationservice.service.ChatService;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatWsController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chatroom/{chatId}/send")
    @PreAuthorize("hasAnyRole('VERIFIED_RECRUITER', 'VERIFIED_CANDIDATE') and @accessChecker.hasAccessToChat(#chatId)")
    public void sendMessage(@DestinationVariable UUID chatId, @Payload String message) {

    }
}
