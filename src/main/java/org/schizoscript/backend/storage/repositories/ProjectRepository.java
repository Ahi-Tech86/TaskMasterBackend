package org.schizoscript.backend.storage.repositories;

import org.schizoscript.backend.storage.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findByName(String name);
    Optional<ProjectEntity> findById(Long id);
}
