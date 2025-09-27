package org.yaroslaavl.communicationservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.yaroslaavl.communicationservice.database.entity.Chat;
import org.yaroslaavl.communicationservice.database.entity.ChatMessage;
import org.yaroslaavl.communicationservice.dto.ChatMessageResponseDto;
import org.yaroslaavl.communicationservice.dto.ChatResponseDto;
import org.yaroslaavl.communicationservice.feignClient.dto.ApplicationChatInfo;
import org.yaroslaavl.communicationservice.mapper.helper.CommonMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CommonMapper.class})
public interface ChatMapper {

    @Mapping(target = "companyLogoUrl", expression = "java(companyUrl(chat, previews))")
    @Mapping(target = "vacancyTitle", expression = "java(vacancyTitle(chat, previews))")
    @Mapping(target = "lastMessage", source = "chat.applicationId", qualifiedByName = "lastMessage")
    @Mapping(target = "lastMessageStatus", source = "chat.applicationId", qualifiedByName = "lastMessageStatus")
    @Mapping(target = "lastMessageDate", source = "chat.applicationId", qualifiedByName = "lastMessageDate")
    @Mapping(target = "isForCurrentUser", source = "chat.applicationId", qualifiedByName = "isForCurrentUser")
    ChatResponseDto toDto(Chat chat, List<ApplicationChatInfo> previews);

    List<ChatResponseDto> toDto(List<Chat> chats, List<ApplicationChatInfo> previews);

    @Mapping(target = "chatId", source = "chat.id")
    @Mapping(target = "sentAt", source = "createdAt")
    ChatMessageResponseDto toMessageDto(ChatMessage chatMessage);

    List<ChatMessageResponseDto> toMessageDto(List<ChatMessage> chatMessages);

    default String companyUrl(Chat chat, List<ApplicationChatInfo> previews) {
        return previews.stream()
                .filter(preview -> preview.applicationId().equals(chat.getApplicationId()))
                .map(ApplicationChatInfo::companyLogoUrl)
                .findFirst()
                .orElse(null);
    }

    default String vacancyTitle(Chat chat, List<ApplicationChatInfo> previews) {
        return previews.stream()
                .filter(preview -> preview.applicationId().equals(chat.getApplicationId()))
                .map(ApplicationChatInfo::vacancyTitle)
                .findFirst()
                .orElse("Vacancy");
    }
}
