package org.yaroslaavl.communicationservice.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.yaroslaavl.communicationservice.database.entity.enums.CallStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "video", schema = "communication_data")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String recruiterId;
    private String candidateId;

    @Column(name = "application_id", nullable = false)
    UUID applicationId;

    private String pin;
    private LocalDateTime pinExpiresAt;

    private Boolean recruiterJoined;
    private Boolean candidateJoined;

    @Enumerated(EnumType.STRING)
    private CallStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}

