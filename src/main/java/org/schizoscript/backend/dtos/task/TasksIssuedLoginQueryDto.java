package org.schizoscript.backend.dtos.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TasksIssuedLoginQueryDto {
    private String login;
}
