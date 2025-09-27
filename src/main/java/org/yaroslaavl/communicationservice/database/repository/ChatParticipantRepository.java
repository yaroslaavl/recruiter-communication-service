package org.yaroslaavl.communicationservice.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaroslaavl.communicationservice.database.entity.ChatParticipant;

import java.util.UUID;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, UUID> {
}
