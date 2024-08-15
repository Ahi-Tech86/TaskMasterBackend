package org.schizoscript.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.schizoscript.backend.dtos.task.CreateTaskRequestDto;
import org.schizoscript.backend.dtos.task.TaskDto;
import org.schizoscript.backend.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/{userId}/project/{projectId}/task/create")
    public ResponseEntity<TaskDto> createTask(
            @PathVariable Long userId, @PathVariable Long projectId, @RequestBody CreateTaskRequestDto request
    ) {
        return ResponseEntity.ok(taskService.createTask(userId, projectId, request));
    }
}
