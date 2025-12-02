package org.yaroslaavl.communicationservice.service;

import org.yaroslaavl.communicationservice.dto.ChatMessageResponseDto;
import org.yaroslaavl.communicationservice.dto.ChatResponseDto;

import java.util.List;
import java.util.UUID;

public interface ChatService {

    void createChat(String candidateId, UUID applicationId);

    void sendMessage(UUID chatId, String content, String senderId);

    List<ChatResponseDto> getChatsCandidate();

    ChatResponseDto getChatRecruiter(UUID applicationId);

    List<ChatMessageResponseDto> findAllMessages(UUID chatId);

    boolean checkChatExist(UUID applicationId);
}
