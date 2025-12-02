package org.yaroslaavl.communicationservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.yaroslaavl.communicationservice.config.properties.WebSocketProperties;
import org.yaroslaavl.communicationservice.database.entity.Chat;
import org.yaroslaavl.communicationservice.database.entity.ChatMessage;
import org.yaroslaavl.communicationservice.database.entity.ChatParticipant;
import org.yaroslaavl.communicationservice.database.entity.enums.MessageStatus;
import org.yaroslaavl.communicationservice.database.repository.ChatMessageRepository;
import org.yaroslaavl.communicationservice.database.repository.ChatParticipantRepository;
import org.yaroslaavl.communicationservice.database.repository.ChatRepository;
import org.yaroslaavl.communicationservice.dto.ChatMessageResponseDto;
import org.yaroslaavl.communicationservice.dto.ChatResponseDto;
import org.yaroslaavl.communicationservice.exception.ContentException;
import org.yaroslaavl.communicationservice.feignClient.RecruitingFeignClient;
import org.yaroslaavl.communicationservice.feignClient.dto.ApplicationChatInfo;
import org.yaroslaavl.communicationservice.mapper.ChatMapper;
import org.yaroslaavl.communicationservice.service.ChatService;
import org.yaroslaavl.communicationservice.service.SecurityContextService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private static final String SYSTEM_MESSAGE = "The chat has been created";

    private final SimpMessagingTemplate messagingTemplate;
    private final RecruitingFeignClient recruitingFeignClient;

    private final SecurityContextService securityContextService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRepository chatRepository;
    private final ChatMapper mapper;
    private final WebSocketProperties properties;

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
    @Transactional
    public void sendMessage(UUID chatId, String content, String senderId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        if (content == null || content.isEmpty()) {
            throw new ContentException("Provided content has error");
        }

        ChatMessageResponseDto chatMessageResponseDto = messageCreator(chat, content, Boolean.FALSE, senderId);

        String recipientId = chat.getParticipants()
                .stream()
                .map(ChatParticipant::getUserId)
                .filter(id -> !id.equals(senderId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Recipient not found"));
        chatMessageRepository.saveAndFlush(mapper.toEntity(chatMessageResponseDto));
        String destination = "/queue/chatroom." + chatId + "." + recipientId;
        messagingTemplate.convertAndSend(destination, chatMessageResponseDto);
    }

    @Override
    public List<ChatResponseDto> getChatsCandidate() {
        List<Chat> chats = chatRepository.findAllByUserId(securityContextService.getAuthenticatedUserInfo());

        Set<UUID> applicationIds = chats.stream()
                .map(Chat::getApplicationId)
                .collect(Collectors.toSet());

        List<ApplicationChatInfo> previews = recruitingFeignClient.getPreviewApplications(applicationIds);

        return mapper.toDto(chats, previews);
    }

    @Override
    public ChatResponseDto getChatRecruiter(UUID applicationId) {
        Optional<Chat> chat = chatRepository.findByUserIdAndApplicationId(securityContextService.getAuthenticatedUserInfo(), applicationId);

        List<ApplicationChatInfo> previews = recruitingFeignClient.getPreviewApplications(Set.of(applicationId));
        return mapper.toDto(chat.get(), previews);
    }

    @Override
    @Transactional
    public List<ChatMessageResponseDto> findAllMessages(UUID chatId) {
        log.info("Attempting to find all chat messages by chat id {}", chatId);
        List<ChatMessage> messages = chatMessageRepository.findChatMessageByChat_Id(chatId, Limit.of(100));

        checkAndMarkMessagesAsDelivered(messages);
        return mapper.toMessageDto(messages);
    }

    @Override
    public boolean checkChatExist(UUID applicationId) {
        return chatRepository.existsByApplicationId(applicationId);
    }

    private void checkAndMarkMessagesAsDelivered(List<ChatMessage> messages) {
        List<ChatMessage> updatedMessages = messages.stream()
                .filter(m -> m.getStatus() == MessageStatus.SENT)
                .filter(m -> !m.getSenderId().equals(securityContextService.getAuthenticatedUserInfo()))
                .peek(m -> m.setStatus(MessageStatus.DELIVERED))
                .toList();

        if (!updatedMessages.isEmpty()) {
            log.info("Marking {} messages as delivered", updatedMessages.size());
            chatMessageRepository.saveAllAndFlush(messages);
        }
    }

    private void chatCreator(String candidateId, UUID applicationId) {
        Chat chat = new Chat();
        chat.setApplicationId(applicationId);
        chat.setCreatedAt(LocalDateTime.now());
        Chat createdChat = chatRepository.saveAndFlush(chat);

        chatParticipantRepository.saveAllAndFlush(
                List.of(newParticipant(createdChat, candidateId),
                        newParticipant(createdChat, securityContextService.getAuthenticatedUserInfo())));

        ChatMessageResponseDto chatMessageResponseDto = messageCreator(chat, SYSTEM_MESSAGE, Boolean.TRUE, null);
        chatMessageRepository.saveAndFlush(mapper.toEntity(chatMessageResponseDto));
    }

    private ChatParticipant newParticipant(Chat chat, String userId) {
        ChatParticipant participant = new ChatParticipant();
        participant.setChat(chat);
        participant.setUserId(userId);
        return participant;
    }

    private ChatMessageResponseDto messageCreator(Chat chat, String content, boolean isFirstMessage, String senderId) {
        ChatMessage message = new ChatMessage();
        message.setChat(chat);
        message.setSenderId(isFirstMessage
                ? "SYSTEM"
                : senderId);
        message.setContent(content);
        message.setStatus(MessageStatus.SENT);
        message.setCreatedAt(LocalDateTime.now());
        return mapper.toMessageDto(message);
    }
}
