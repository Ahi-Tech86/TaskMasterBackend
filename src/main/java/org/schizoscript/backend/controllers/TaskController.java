package org.schizoscript.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.schizoscript.backend.dtos.task.CreateTaskRequestDto;
import org.schizoscript.backend.dtos.task.TaskDto;
import org.schizoscript.backend.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{userId}/project/{projectId}/task/{taskId}")
    public ResponseEntity<TaskDto> getTaskById(
            @PathVariable Long userId, @PathVariable Long projectId, @PathVariable Long taskId
    ) {
        return ResponseEntity.ok(taskService.showTaskById(userId, projectId, taskId));
    }

    @GetMapping("/{userId}/project/{projectId}/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks(@PathVariable Long userId, @PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getAllTasks(userId, projectId));
    }
}
