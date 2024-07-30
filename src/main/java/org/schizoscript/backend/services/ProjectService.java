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

    private final String MANAGER_ROLE = "PROJECT_MANAGER_ROLE";

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

        checkingUserOnRolePrivileges(userId, MANAGER_ROLE);

        validateProjectModificationRequest(projectModificationRequest);

        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(
                () -> new AppException("Project not found", HttpStatus.NOT_FOUND)
        );

        if (projectModificationRequest.getName() != null) {
            checkProjectNameUniqueness(projectModificationRequest.getName());
            project.setName(projectModificationRequest.getName());
        }

        if (projectModificationRequest.getDescription() != null) {
            project.setDescription(projectModificationRequest.getDescription());
        }

        ProjectEntity savedProject = projectRepository.save(project);

        return projectDtoFactory.makeProjectDto(savedProject);
    }

    public void deleteProject(Long userId, Long projectId) {

        checkingUserOnRolePrivileges(userId, MANAGER_ROLE);
    }

    private void checkProjectNameUniqueness(String projectName) {
        Optional<ProjectEntity> optionalProject = projectRepository.findByName(projectName);
        if (optionalProject.isPresent()) {
            throw new AppException(
                    "You can't rename this project because project with this name already exists", HttpStatus.BAD_REQUEST
            );
        }
    }

    private void checkingUserOnRolePrivileges(Long userId, String roleName) {
        Optional<ProjectUsersRolesEntity> optionalProjectUsersRolesEntity = projectUsersRoleRepository.findByUserId(userId);
        ProjectUsersRolesEntity projectUsersRolesEntity = optionalProjectUsersRolesEntity.orElseThrow(
                () -> new AppException("You don't have permission to access this action", HttpStatus.FORBIDDEN)
        );

        String userRole = projectUsersRolesEntity.getProjectRole().toString();
        if (!userRole.equals(roleName)) {
            throw new AppException("You don't have permission to access this action", HttpStatus.FORBIDDEN);
        }
    }

    private void validateProjectModificationRequest(ProjectModificationRequest request) {
        if (request.getName() == null && request.getDescription() == null) {
            throw new AppException("Bad request", HttpStatus.BAD_REQUEST);
        }
    }
}
