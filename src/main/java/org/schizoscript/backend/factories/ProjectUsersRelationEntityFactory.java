package org.schizoscript.backend.factories;

import org.schizoscript.backend.dtos.ProjectDto;
import org.schizoscript.backend.storage.entities.ProjectUsersRelationEntity;
import org.schizoscript.backend.storage.enums.ProjectRole;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ProjectUsersRelationEntityFactory {

    public ProjectUsersRelationEntity makeProjectUsersRelationEntity(ProjectDto projectDto, Long userId) {

        return ProjectUsersRelationEntity
                .builder()
                .projectId(projectDto.getId())
                .userId(userId)
                .build();
    }
}
