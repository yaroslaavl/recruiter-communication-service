package org.yaroslaavl.communicationservice.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaroslaavl.communicationservice.database.entity.Video;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {

    Optional<Video> findByPin(String pin);

    boolean existsByApplicationId(UUID applicationId);
}
