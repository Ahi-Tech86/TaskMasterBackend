package org.schizoscript.backend.storage.repositories;

import org.schizoscript.backend.storage.entities.ProjectMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMemberEntity, Long> {
    Optional<ProjectMemberEntity> findByProjectIdAndUserId(Long projectId, Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM project_users WHERE project_id = :projectId", nativeQuery = true)
    void deleteAllItemsByProjectId(Long projectId);
}
