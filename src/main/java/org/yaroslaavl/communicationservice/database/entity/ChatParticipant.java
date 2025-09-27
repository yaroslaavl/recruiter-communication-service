package org.yaroslaavl.communicationservice.database.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "chat_participant", schema = "communication_data")
public class ChatParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    Chat chat;

    @Column(name = "user_id", nullable = false)
    String userId;
}
