package org.schizoscript.backend.dtos.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TasksStatusQueryDto {
    @JsonProperty("status")
    private String taskStatus;
}
