package org.yaroslaavl.communicationservice.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yaroslaavl.communicationservice.database.entity.Chat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {

    @Query("""
    SELECT c FROM Chat c
    WHERE (:chatId IS NULL OR c.id = :chatId)
    AND (:userId IS NULL OR EXISTS(
        SELECT cp FROM ChatParticipant cp WHERE cp.userId = :userId
    ))
    """)
    Optional<Chat> findByChatIdAndUserId(UUID chatId, String userId);

    @Query("""
    SELECT c FROM Chat c
    WHERE (:userId IS NULL OR EXISTS(
        SELECT cp FROM ChatParticipant cp WHERE cp.userId = :userID
    ))
    """)
    List<Chat> findAllByUserId(String userId);
}
