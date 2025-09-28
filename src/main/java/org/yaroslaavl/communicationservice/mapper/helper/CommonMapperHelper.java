package org.yaroslaavl.communicationservice.mapper.helper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.yaroslaavl.communicationservice.database.entity.ChatMessage;
import org.yaroslaavl.communicationservice.database.entity.enums.MessageStatus;
import org.yaroslaavl.communicationservice.database.repository.ChatMessageRepository;
import org.yaroslaavl.communicationservice.service.SecurityContextService;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommonMapperHelper {

    private final SecurityContextService securityContextService;
    private final ChatMessageRepository chatMessageRepository;

    @Named("lastMessage")
    public String lastMessage(UUID chatId) {
        return getLastChatMessage(chatId).getContent();
    }

    @Named("lastMessageStatus")
    public MessageStatus lastMessageStatus(UUID chatId) {
        return getLastChatMessage(chatId).getStatus();
    }

    @Named("lastMessageDate")
    public LocalDateTime lastMessageDate(UUID chatId) {
        return getLastChatMessage(chatId).getCreatedAt();
    }

    @Named("isForCurrentUser")
    public boolean isForCurrentUser(UUID chatId) {
        return getLastChatMessage(chatId)
                .getSenderId()
                .equals(securityContextService.getAuthenticatedUserInfo());
    }

    private ChatMessage getLastChatMessage(UUID chatId) {
        return chatMessageRepository.findFirstByChat_IdOrderByCreatedAtDesc(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Message not found"));
    }
}
