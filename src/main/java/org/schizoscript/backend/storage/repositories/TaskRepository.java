package org.schizoscript.backend.storage.repositories;

import org.schizoscript.backend.storage.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Optional<TaskEntity> findById(Long taskId);

    Optional<List<TaskEntity>> findByProjectId(Long projectId);
}
