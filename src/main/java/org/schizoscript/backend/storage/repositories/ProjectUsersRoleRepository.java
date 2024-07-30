package org.schizoscript.backend.storage.repositories;

import org.schizoscript.backend.storage.entities.ProjectUsersRolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectUsersRoleRepository extends JpaRepository<ProjectUsersRolesEntity, Long> {
    Optional<ProjectUsersRolesEntity> findByUserId(Long userId);
}
