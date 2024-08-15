package org.schizoscript.backend.storage.repositories;

import org.schizoscript.backend.storage.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
