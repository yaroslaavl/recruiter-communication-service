package org.yaroslaavl.communicationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yaroslaavl.communicationservice.dto.ChatMessageResponseDto;
import org.yaroslaavl.communicationservice.dto.ChatResponseDto;
import org.yaroslaavl.communicationservice.service.ChatService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/{applicationId}/create")
    @PreAuthorize("hasRole('VERIFIED_RECRUITER')")
    public ResponseEntity<Void> createChat(@PathVariable("applicationId") UUID applicationId,
                                           @RequestParam("candidateId") String candidateId) {
        chatService.createChat(candidateId, applicationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAnyRole('VERIFIED_CANDIDATE', 'VERIFIED_RECRUITER')")
    public ResponseEntity<List<ChatResponseDto>> getChats() {
        return ResponseEntity.ok(chatService.getChats());
    }

    @GetMapping("/{chatId}/messages")
    @PreAuthorize("hasAnyRole('VERIFIED_CANDIDATE', 'VERIFIED_RECRUITER') and @accessChecker.hasAccessToChat(#chatId)")
    public ResponseEntity<List<ChatMessageResponseDto>> getChatMessages(@PathVariable("chatId") UUID chatId) {
        return ResponseEntity.ok(chatService.findAllMessages(chatId));
    }
}
