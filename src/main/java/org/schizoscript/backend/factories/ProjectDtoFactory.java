package org.schizoscript.backend.factories;

import org.schizoscript.backend.dtos.project.ProjectDto;
import org.schizoscript.backend.storage.entities.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoFactory {

    public ProjectDto makeProjectDto(ProjectEntity projectEntity) {

        return ProjectDto
                .builder()
                .id(projectEntity.getId())
                .name(projectEntity.getName())
                .description(projectEntity.getDescription())
                .ownerUserId(projectEntity.getOwnerUserId())
                .createAt(projectEntity.getCreateAt().toString())
                .build();
    }
}
