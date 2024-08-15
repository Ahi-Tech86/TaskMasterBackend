package org.schizoscript.backend.factories;

import org.schizoscript.backend.dtos.task.CreateTaskRequestDto;
import org.schizoscript.backend.storage.entities.TaskEntity;
import org.schizoscript.backend.storage.enums.TaskPriority;
import org.schizoscript.backend.storage.enums.TaskStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class TaskEntityFactory {

    public TaskEntity makeTaskEntity(CreateTaskRequestDto requestDto, Long userId, Long projectId) {

        Instant now = Instant.now();
        Instant endAt = now.plus(requestDto.getDeadlineInDays(), ChronoUnit.DAYS);

        TaskPriority taskPriority = switch (requestDto.getPriority().toUpperCase()) {
            case "HIGH" -> TaskPriority.HIGH_PRIORITY;
            case "MEDIUM" -> TaskPriority.MEDIUM_PRIORITY;
            default -> TaskPriority.LOW_PRIORITY;
        };

        return TaskEntity.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .ownerId(userId)
                .issuedId(requestDto.getIssuedId())
                .endAt(endAt)
                .projectId(projectId)
                .status(TaskStatus.TO_DO)
                .priority(taskPriority)
                .build();

    }
}
