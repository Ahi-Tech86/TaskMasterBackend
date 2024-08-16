package org.schizoscript.backend.factories;

import org.schizoscript.backend.dtos.project.ProjectMemberDto;
import org.schizoscript.backend.storage.entities.ProjectMemberEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectMemberDtoFactory {

    public ProjectMemberDto makeProjectMemberDto(ProjectMemberEntity projectMemberEntity) {

        return ProjectMemberDto.builder()
                .id(projectMemberEntity.getId())
                .projectId(projectMemberEntity.getProjectId())
                .role(projectMemberEntity.getProjectRole())
                .userId(projectMemberEntity.getUserId())
                .build();
    }
}
