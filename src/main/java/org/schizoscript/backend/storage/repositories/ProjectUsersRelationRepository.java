package org.schizoscript.backend.storage.repositories;

import org.schizoscript.backend.storage.entities.ProjectUsersRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProjectUsersRelationRepository extends JpaRepository<ProjectUsersRelationEntity, Long> {
    Optional<ProjectUsersRelationEntity> findByProjectIdAndUserId(Long projectId, Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM project_users WHERE project_id = :projectId", nativeQuery = true)
    void deleteAllItemsByProjectId(Long projectId);
}
