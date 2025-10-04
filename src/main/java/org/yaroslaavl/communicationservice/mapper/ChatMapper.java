package org.yaroslaavl.communicationservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.yaroslaavl.communicationservice.database.entity.Chat;
import org.yaroslaavl.communicationservice.database.entity.ChatMessage;
import org.yaroslaavl.communicationservice.dto.ChatMessageResponseDto;
import org.yaroslaavl.communicationservice.dto.ChatResponseDto;
import org.yaroslaavl.communicationservice.feignClient.dto.ApplicationChatInfo;
import org.yaroslaavl.communicationservice.mapper.helper.CommonMapperHelper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CommonMapperHelper.class})
public interface ChatMapper {

    @Mapping(target = "companyLogoUrl", expression = "java(companyUrl(chat, previews))")
    @Mapping(target = "vacancyTitle", expression = "java(vacancyTitle(chat, previews))")
    @Mapping(target = "lastMessage", source = "chat.id", qualifiedByName = "lastMessage")
    @Mapping(target = "lastMessageStatus", source = "chat.id", qualifiedByName = "lastMessageStatus")
    @Mapping(target = "lastMessageDate", source = "chat.id", qualifiedByName = "lastMessageDate")
    @Mapping(target = "isForCurrentUser", source = "chat.id", qualifiedByName = "isForCurrentUser")
    ChatResponseDto toDto(Chat chat, List<ApplicationChatInfo> previews);

     default List<ChatResponseDto> toDto(List<Chat> chats, List<ApplicationChatInfo> previews) {
         return chats.stream()
                 .map(chat -> toDto(chat, previews))
                 .toList();
     }

    @Mapping(target = "chatId", source = "chat.id")
    @Mapping(target = "sentAt", source = "createdAt")
    ChatMessageResponseDto toMessageDto(ChatMessage chatMessage);

    @Mapping(target = "chat.id", source = "chatId")
    @Mapping(target = "createdAt", source = "sentAt")
    ChatMessage toEntity(ChatMessageResponseDto dto);

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
