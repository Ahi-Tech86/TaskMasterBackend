package org.schizoscript.backend.services;

import lombok.RequiredArgsConstructor;
import org.schizoscript.backend.dtos.DeleteResponseDto;
import org.schizoscript.backend.dtos.ProjectModificationRequest;
import org.schizoscript.backend.dtos.ProjectDto;
import org.schizoscript.backend.exceptions.AppException;
import org.schizoscript.backend.factories.ProjectDtoFactory;
import org.schizoscript.backend.factories.ProjectEntityFactory;
import org.schizoscript.backend.factories.ProjectUsersRelationEntityFactory;
import org.schizoscript.backend.storage.entities.ProjectEntity;
import org.schizoscript.backend.storage.entities.ProjectUsersRelationEntity;
import org.schizoscript.backend.storage.repositories.ProjectRepository;
import org.schizoscript.backend.storage.repositories.ProjectUsersRelationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final String MANAGER_ROLE = "PROJECT_MANAGER_ROLE";

    private final ProjectRepository projectRepository;
    private final ProjectDtoFactory projectDtoFactory;
    private final ProjectEntityFactory projectEntityFactory;
    private final ProjectUsersRelationRepository projectUsersRelationRepository;
    private final ProjectUsersRelationEntityFactory projectUsersRelationEntityFactory;

    @Transactional
    public ProjectDto createProject(ProjectModificationRequest request, Long userId) {
        checkProjectNameUniqueness(request.getName());

        ProjectEntity project = projectEntityFactory.makeProjectEntity(request, userId);
        ProjectEntity savedProject = projectRepository.save(project);

        ProjectDto responseProjectDto = projectDtoFactory.makeProjectDto(savedProject);
        ProjectUsersRelationEntity projectUsersRelationEntity = projectUsersRelationEntityFactory
                .makeProjectUsersRelationEntity(responseProjectDto, userId);
        projectUsersRelationRepository.save(projectUsersRelationEntity);

        return responseProjectDto;
    }

    @Transactional
    public ProjectDto editProject(ProjectModificationRequest request, Long userId, Long projectId) {

        checkingUserOnRolePrivileges(projectId, userId, MANAGER_ROLE);

        validateProjectModificationRequest(request);

        ProjectEntity project = checkProjectExistsByProjectId(projectId);

        if (request.getName() != null) {
            checkProjectNameUniqueness(request.getName());
            project.setName(request.getName());
        }

        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }

        ProjectEntity savedProject = projectRepository.save(project);

        return projectDtoFactory.makeProjectDto(savedProject);
    }

    @Transactional
    public DeleteResponseDto deleteProject(Long userId, Long projectId) {

        checkingUserOnRolePrivileges(projectId, userId, MANAGER_ROLE);

        ProjectEntity deletedProject = checkProjectExistsByProjectId(projectId);

        projectRepository.deleteById(projectId);

        return DeleteResponseDto
                .builder()
                .answer("Project with name " + deletedProject.getName() + "was successfully deleted.")
                .build();
    }

    private void checkProjectNameUniqueness(String projectName) {
        Optional<ProjectEntity> optionalProject = projectRepository.findByName(projectName);
        if (optionalProject.isPresent()) {
            throw new AppException(
                    "You can't rename this project because project with this name already exists", HttpStatus.BAD_REQUEST
            );
        }
    }

    private ProjectEntity checkProjectExistsByProjectId(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                () -> new AppException("Project not found", HttpStatus.NOT_FOUND)
        );
    }

    private void checkingUserOnRolePrivileges(Long projectId, Long userId, String roleName) {
        Optional<ProjectUsersRelationEntity> optionalProjectUsersRolesEntity = projectUsersRelationRepository
                .findByProjectIdAndUserId(projectId, userId);
        ProjectUsersRelationEntity projectUsersRelationEntity = optionalProjectUsersRolesEntity.orElseThrow(
                () -> new AppException("You don't have permission to access this action", HttpStatus.FORBIDDEN)
        );

        String userRole = projectUsersRelationEntity.getProjectRole().toString();
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
