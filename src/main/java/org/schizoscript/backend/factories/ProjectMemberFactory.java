package org.schizoscript.backend.factories;

import org.schizoscript.backend.dtos.project.ProjectDto;
import org.schizoscript.backend.storage.entities.ProjectMemberEntity;
import org.schizoscript.backend.storage.enums.ProjectRole;
import org.springframework.stereotype.Component;

@Component
public class ProjectMemberFactory {

    public ProjectMemberEntity makeProjectMemberForManagerRole(ProjectDto projectDto, Long userId) {

        return ProjectMemberEntity
                .builder()
                .projectId(projectDto.getId())
                .userId(userId)
                .projectRole(ProjectRole.PROJECT_MANAGER_ROLE)
                .build();
    }

    public ProjectMemberEntity makeProjectMemberForDeveloperRole(Long projectId, Long userId) {

        return ProjectMemberEntity
                .builder()
                .projectId(projectId)
                .userId(userId)
                .projectRole(ProjectRole.PROJECT_DEVELOPER_ROLE)
                .build();
    }
}
