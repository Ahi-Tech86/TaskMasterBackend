package org.schizoscript.backend.factories;

import org.schizoscript.backend.storage.entities.ProjectUsersRolesEntity;
import org.schizoscript.backend.storage.enums.ProjectRole;
import org.springframework.stereotype.Component;

@Component
public class ProjectUsersRoleFactory {

    public ProjectUsersRolesEntity makeProjectUsersRolesEntity(Long userId) {

        return ProjectUsersRolesEntity
                .builder()
                .userId(userId)
                .projectRole(ProjectRole.PROJECT_MANAGER_ROLE)
                .build();
    }
}
