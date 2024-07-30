package org.schizoscript.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.schizoscript.backend.dtos.ProjectModificationRequest;
import org.schizoscript.backend.dtos.ProjectDto;
import org.schizoscript.backend.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//    @DeleteMapping("/{userId}/project/{projectId}/delete")
//    public ResponseEntity<ProjectDto> deleteProject(@PathVariable Long userId, @PathVariable Long projectId) {
//        return ResponseEntity.ok(projectService.deleteProject(userId, projectId));
//    }
}
