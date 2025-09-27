package org.yaroslaavl.communicationservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.yaroslaavl.communicationservice.database.entity.Chat;
import org.yaroslaavl.communicationservice.database.entity.ChatMessage;
import org.yaroslaavl.communicationservice.database.entity.ChatParticipant;
import org.yaroslaavl.communicationservice.database.entity.enums.MessageStatus;
import org.yaroslaavl.communicationservice.database.repository.ChatMessageRepository;
import org.yaroslaavl.communicationservice.database.repository.ChatParticipantRepository;
import org.yaroslaavl.communicationservice.database.repository.ChatRepository;
import org.yaroslaavl.communicationservice.dto.ChatMessageResponseDto;
import org.yaroslaavl.communicationservice.dto.ChatResponseDto;
import org.yaroslaavl.communicationservice.feignClient.RecruitingFeignClient;
import org.yaroslaavl.communicationservice.feignClient.dto.ApplicationChatInfo;
import org.yaroslaavl.communicationservice.mapper.ChatMapper;
import org.yaroslaavl.communicationservice.service.ChatService;
import org.yaroslaavl.communicationservice.service.SecurityContextService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private static final String SYSTEM_MESSAGE = "The chat has been created";

    private final RecruitingFeignClient recruitingFeignClient;

    private final SecurityContextService securityContextService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRepository chatRepository;
    private final ChatMapper mapper;

    @Override
    @Transactional
    public void createChat(String candidateId, UUID applicationId) {
        boolean isOpenedForChatting = recruitingFeignClient.isOpenedChatting(applicationId);

        if (isOpenedForChatting) {
            chatCreator(candidateId, applicationId);
            log.info("The chat has been created");
        }
    }

    @Override
    public ChatMessageResponseDto sendMessage(UUID chatId, String content) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        ChatMessage chatMessage = messageCreator(chat, content, Boolean.FALSE);
        return mapper.toMessageDto(chatMessageRepository.save(chatMessage));
    }

    @Override
    public List<ChatResponseDto> getChats() {
        List<Chat> chats = chatRepository.findAllByUserId(securityContextService.getAuthenticatedUserInfo());

        Set<UUID> applicationIds = chats.stream()
                .map(Chat::getApplicationId)
                .collect(Collectors.toSet());

        List<ApplicationChatInfo> previews = recruitingFeignClient.getPreviewApplications(applicationIds);

        return mapper.toDto(chats, previews);
    }

    @Override
    public List<ChatMessageResponseDto> findAllMessages(UUID chatId) {
        log.info("Attempting to find all chat messages by chat id {}", chatId);
        List<ChatMessage> messages = chatMessageRepository.findChatMessageByChat_Id(chatId, Limit.of(100));

        messages.stream()
                .filter(message -> message.getStatus() == MessageStatus.SENT)
                .filter(message -> !message.getSenderId().equals(securityContextService.getAuthenticatedUserInfo()))
                .forEach(message -> {
                    message.setStatus(MessageStatus.DELIVERED);
                    chatMessageRepository.save(message);
                });

        return mapper.toMessageDto(messages);
    }

    private void chatCreator(String candidateId, UUID applicationId) {
        Chat chat = new Chat();
        chat.setApplicationId(applicationId);
        chat.setCreatedAt(LocalDateTime.now());
        Chat createdChat = chatRepository.saveAndFlush(chat);

        chatParticipantRepository.saveAllAndFlush(
                List.of(newParticipant(createdChat, candidateId),
                        newParticipant(createdChat, securityContextService.getAuthenticatedUserInfo())));

        ChatMessage chatMessage = messageCreator(chat, SYSTEM_MESSAGE, Boolean.TRUE);
        chatMessageRepository.saveAndFlush(chatMessage);
    }

    private ChatParticipant newParticipant(Chat chat, String userId) {
        ChatParticipant participant = new ChatParticipant();
        participant.setChat(chat);
        participant.setUserId(userId);
        return participant;
    }

    private ChatMessage messageCreator(Chat chat, String content, boolean isFirstMessage) {
        ChatMessage message = new ChatMessage();
        message.setChat(chat);
        message.setSenderId(isFirstMessage
                ? "SYSTEM"
                : securityContextService.getAuthenticatedUserInfo());
        message.setContent(content);
        message.setStatus(MessageStatus.SENT);
        message.setCreatedAt(LocalDateTime.now());
        return message;
    }
}
