package org.schizoscript.backend.storage.repositories;

import org.schizoscript.backend.storage.entities.ProjectUsersRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectUsersRelationRepository extends JpaRepository<ProjectUsersRelationEntity, Long> {
    Optional<ProjectUsersRelationEntity> findByProjectIdAndUserId(Long projectId, Long userId);
}
