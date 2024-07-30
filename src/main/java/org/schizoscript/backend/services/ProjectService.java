package org.schizoscript.backend.services;

import lombok.RequiredArgsConstructor;
import org.schizoscript.backend.dtos.ProjectModificationRequest;
import org.schizoscript.backend.dtos.ProjectDto;
import org.schizoscript.backend.exceptions.AppException;
import org.schizoscript.backend.factories.ProjectDtoFactory;
import org.schizoscript.backend.factories.ProjectEntityFactory;
import org.schizoscript.backend.factories.ProjectUsersRelationEntityFactory;
import org.schizoscript.backend.factories.ProjectUsersRoleFactory;
import org.schizoscript.backend.storage.entities.ProjectEntity;
import org.schizoscript.backend.storage.entities.ProjectUsersRelationEntity;
import org.schizoscript.backend.storage.entities.ProjectUsersRolesEntity;
import org.schizoscript.backend.storage.repositories.ProjectRepository;
import org.schizoscript.backend.storage.repositories.ProjectUsersRelationRepository;
import org.schizoscript.backend.storage.repositories.ProjectUsersRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectDtoFactory projectDtoFactory;
    private final ProjectEntityFactory projectEntityFactory;
    private final ProjectUsersRoleFactory projectUsersRoleFactory;
    private final ProjectUsersRoleRepository projectUsersRoleRepository;
    private final ProjectUsersRelationRepository projectUsersRelationRepository;
    private final ProjectUsersRelationEntityFactory projectUsersRelationEntityFactory;

    public ProjectDto createProject(ProjectModificationRequest projectModificationRequest, Long userId) {
        Optional<ProjectEntity> optionalProject = projectRepository.findByName(projectModificationRequest.getName());

        if (optionalProject.isPresent()) {
            throw new AppException("Project already exists", HttpStatus.BAD_REQUEST);
        }

        ProjectEntity project = projectEntityFactory.makeProjectEntity(projectModificationRequest, userId);
        ProjectEntity savedProject = projectRepository.save(project);

        ProjectDto responseProjectDto = projectDtoFactory.makeProjectDto(savedProject);
        ProjectUsersRelationEntity projectUsersRelationEntity = projectUsersRelationEntityFactory
                .makeProjectUsersRelationEntity(responseProjectDto, userId);
        projectUsersRelationRepository.save(projectUsersRelationEntity);

        ProjectUsersRolesEntity projectUsersRolesEntity = projectUsersRoleFactory.makeProjectUsersRolesEntity(userId);
        projectUsersRoleRepository.save(projectUsersRolesEntity);

        return responseProjectDto;
    }

    public ProjectDto editProject(ProjectModificationRequest projectModificationRequest, Long userId, Long projectId) {

        Optional<ProjectUsersRolesEntity> optionalProjectUsersRolesEntity = projectUsersRoleRepository.findByUserId(userId);
        String userRole = optionalProjectUsersRolesEntity.get().getProjectRole().toString();

        if (!userRole.equals("PROJECT_MANAGER_ROLE")) {
            throw new AppException("You don't have permission to access this action", HttpStatus.FORBIDDEN);
        }

        if (projectModificationRequest.getName() != null && projectModificationRequest.getDescription() != null) {
            Optional<ProjectEntity> optionalProject = projectRepository.findByName(projectModificationRequest.getName());

            if (optionalProject.isPresent()) {
                throw new AppException("You can't rename this project because project with this name already exists", HttpStatus.BAD_REQUEST);
            }

            ProjectEntity project = projectRepository.findById(projectId).get();
            project.setName(projectModificationRequest.getName());
            project.setDescription(projectModificationRequest.getDescription());

            ProjectEntity savedProject = projectRepository.save(project);

            return projectDtoFactory.makeProjectDto(savedProject);

        } else if (projectModificationRequest.getName() != null) {

            Optional<ProjectEntity> optionalProject = projectRepository.findByName(projectModificationRequest.getName());

            if (optionalProject.isPresent()) {
                throw new AppException("You can't rename this project because project with this name already exists", HttpStatus.BAD_REQUEST);
            }

            ProjectEntity project = projectRepository.findById(projectId).get();
            project.setName(projectModificationRequest.getName());

            ProjectEntity savedProject = projectRepository.save(project);

            return projectDtoFactory.makeProjectDto(savedProject);

        } else if (projectModificationRequest.getDescription() != null) {

            ProjectEntity project = projectRepository.findById(projectId).get();
            project.setDescription(projectModificationRequest.getDescription());

            ProjectEntity savedProject = projectRepository.save(project);

            return projectDtoFactory.makeProjectDto(savedProject);

        } else {
            throw new AppException("Bad request", HttpStatus.BAD_REQUEST);
        }
    }
}
