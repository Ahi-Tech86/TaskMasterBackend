package org.schizoscript.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.schizoscript.backend.dtos.LoginDto;
import org.schizoscript.backend.dtos.MessageResponseDto;
import org.schizoscript.backend.dtos.project.*;
import org.schizoscript.backend.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{userId}/projects")
    public ResponseEntity<List<ProjectDto>> viewAllProjects(@PathVariable Long userId) {
        return ResponseEntity.ok(projectService.getProjects(userId));
    }

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
        return ResponseEntity.ok(projectService.showAllUsersInProject(userId, projectId));
    }

    @PostMapping("/{userId}/project/{projectId}/manage/changeUserRole")
    public ResponseEntity<ProjectMemberDto> changeUserRole(
            @PathVariable Long userId, @PathVariable Long projectId, @RequestBody ChangeUserRoleRequest request
    ) {
        return ResponseEntity.ok(
                projectService.changeUserRole(userId, projectId, request.getLogin(), request.getNewRoleName())
        );
    }

    @DeleteMapping("/{userId}/project/{projectId}/manage/kickUser")
    public ResponseEntity<MessageResponseDto> kickUser(
            @PathVariable Long userId, @PathVariable Long projectId, @RequestBody LoginDto loginDto
    ) {
        return ResponseEntity.ok(projectService.kickUserFromProject(userId, projectId, loginDto.getLogin()));
    }
}
