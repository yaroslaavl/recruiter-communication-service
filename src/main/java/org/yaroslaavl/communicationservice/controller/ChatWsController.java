package org.yaroslaavl.communicationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.yaroslaavl.communicationservice.dto.ChatRequest;
import org.yaroslaavl.communicationservice.service.ChatService;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatWsController {

    private final ChatService chatService;

    @MessageMapping("/chatroom/{chatId}/send")
    public void sendMessage(@DestinationVariable UUID chatId, @Payload ChatRequest request) {
        chatService.sendMessage(chatId, request.content(), request.senderId());
    }
}
