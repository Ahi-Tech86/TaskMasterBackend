package org.schizoscript.backend.factories;

import org.schizoscript.backend.dtos.ProjectModificationRequest;
import org.schizoscript.backend.storage.entities.ProjectEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ProjectEntityFactory {

    public ProjectEntity makeProjectEntity(ProjectModificationRequest projectModificationRequest, Long ownerUserId) {

        return ProjectEntity
                .builder()
                .name(projectModificationRequest.getName())
                .description(projectModificationRequest.getDescription())
                .ownerUserId(ownerUserId)
                .createAt(Instant.now())
                .build();
    }
}
