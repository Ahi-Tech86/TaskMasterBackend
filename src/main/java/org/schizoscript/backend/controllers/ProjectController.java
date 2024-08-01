package org.schizoscript.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.schizoscript.backend.dtos.LoginDto;
import org.schizoscript.backend.dtos.MessageResponseDto;
import org.schizoscript.backend.dtos.project.ProjectModificationRequest;
import org.schizoscript.backend.dtos.project.ProjectDto;
import org.schizoscript.backend.dtos.project.ProjectUsersDto;
import org.schizoscript.backend.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/{userId}/project/create")
    public ResponseEntity<ProjectDto> createProject(
            @PathVariable Long userId, @RequestBody ProjectModificationRequest projectModificationRequest
    ) {
        return ResponseEntity.ok(projectService.createProject(projectModificationRequest, userId));
    }

    @PostMapping("/{userId}/project/{projectId}/edit")
    public ResponseEntity<ProjectDto> editProject(
            @PathVariable Long userId, @PathVariable Long projectId,
            @RequestBody ProjectModificationRequest projectModificationRequest
    ) {
        return ResponseEntity.ok(projectService.editProject(projectModificationRequest, userId, projectId));
    }

    @DeleteMapping("/{userId}/project/{projectId}/delete")
    public ResponseEntity<MessageResponseDto> deleteProject(@PathVariable Long userId, @PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.deleteProject(userId, projectId));
    }

    @PostMapping("/{userId}/project/{projectId}/invite")
    public ResponseEntity<MessageResponseDto> inviteUserInProject(
            @PathVariable Long userId, @PathVariable Long projectId, @RequestBody LoginDto loginDto
    ) {
        return ResponseEntity.ok(projectService.inviteUserInProject(userId, projectId, loginDto.getLogin()));
    }

    @GetMapping("/{userId}/project/{projectId}/manage/usersList")
    public ResponseEntity<List<ProjectUsersDto>> showAllUsersInProject(
            @PathVariable Long userId, @PathVariable Long projectId
    ) {
//        return ResponseEntity.ok(Arrays.toString(projectService.showAllUsersInProject(userId, projectId).toArray()));
        return ResponseEntity.ok(projectService.showAllUsersInProject(userId, projectId));
    }
}
