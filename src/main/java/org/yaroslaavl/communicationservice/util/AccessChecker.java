package org.yaroslaavl.communicationservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yaroslaavl.communicationservice.database.repository.ChatRepository;
import org.yaroslaavl.communicationservice.service.SecurityContextService;


import java.util.UUID;

@Component("accessChecker")
@RequiredArgsConstructor
public class AccessChecker {

    private final SecurityContextService securityContextService;
    private final ChatRepository chatRepository;

    public boolean hasAccessToChat(UUID chatId) {
        String userId = securityContextService.getAuthenticatedUserInfo();
        return userId != null && !userId.isEmpty()
                && chatRepository.findByChatIdAndUserId(chatId, userId).isPresent();
    }
}