package org.yaroslaavl.communicationservice.database.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaroslaavl.communicationservice.database.entity.ChatMessage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    List<ChatMessage> findChatMessageByChat_Id(UUID chatId, Limit limit);

    Optional<ChatMessage> findFirstByChat_IdOrderByCreatedAtDesc(UUID chatId);
}
