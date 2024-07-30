package org.schizoscript.backend.storage.repositories;

import org.schizoscript.backend.storage.entities.ProjectUsersRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectUsersRelationRepository extends JpaRepository<ProjectUsersRelationEntity, Long> {
}
