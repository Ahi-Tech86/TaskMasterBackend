package org.schizoscript.backend.dtos.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.schizoscript.backend.storage.enums.TaskPriority;
import org.schizoscript.backend.storage.enums.TaskStatus;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private Long issuedId;
    private String createAt;
    private String endAt;
    private Long projectId;
    private TaskStatus status;
    private TaskPriority priority;
}
