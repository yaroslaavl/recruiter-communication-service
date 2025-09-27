package org.yaroslaavl.communicationservice.database.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.yaroslaavl.communicationservice.database.entity.enums.MessageStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "chat_message", schema = "communication_data")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    Chat chat;

    @Column(name = "sender_id", nullable = false)
    String senderId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    MessageStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;
}
