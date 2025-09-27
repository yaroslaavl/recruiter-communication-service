package org.yaroslaavl.communicationservice.database.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "chat", schema = "communication_data")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "application_id", nullable = false)
    UUID applicationId;

    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ChatParticipant> participants;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;
}
