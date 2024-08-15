package org.schizoscript.backend.factories;

import org.schizoscript.backend.dtos.task.TaskDto;
import org.schizoscript.backend.storage.entities.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoFactory {

    public TaskDto makeTaskDto(TaskEntity taskEntity) {

        return TaskDto.builder()
                .id(taskEntity.getId())
                .name(taskEntity.getName())
                .description(taskEntity.getDescription())
                .ownerId(taskEntity.getOwnerId())
                .issuedId(taskEntity.getIssuedId())
                .createAt(taskEntity.getCreateAt().toString())
                .endAt(taskEntity.getEndAt().toString())
                .projectId(taskEntity.getProjectId())
                .status(taskEntity.getStatus())
                .priority(taskEntity.getPriority())
                .build();
    }
}
