package org.schizoscript.backend.services;

import lombok.RequiredArgsConstructor;
import org.schizoscript.backend.dtos.MessageResponseDto;
import org.schizoscript.backend.dtos.project.ProjectMemberDto;
import org.schizoscript.backend.dtos.project.ProjectModificationRequest;
import org.schizoscript.backend.dtos.project.ProjectDto;
import org.schizoscript.backend.dtos.project.ProjectUsersDto;
import org.schizoscript.backend.exceptions.AppException;
import org.schizoscript.backend.factories.*;
import org.schizoscript.backend.storage.entities.ProjectEntity;
import org.schizoscript.backend.storage.entities.ProjectMemberEntity;
import org.schizoscript.backend.storage.entities.UserEntity;
import org.schizoscript.backend.storage.enums.ProjectRole;
import org.schizoscript.backend.storage.repositories.ProjectRepository;
import org.schizoscript.backend.storage.repositories.ProjectMemberRepository;
import org.schizoscript.backend.storage.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final String MANAGER_ROLE = "PROJECT_MANAGER_ROLE";

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectDtoFactory projectDtoFactory;
    private final ProjectMemberFactory projectMemberFactory;
    private final ProjectEntityFactory projectEntityFactory;
    private final ProjectMemberDtoFactory projectMemberDtoFactory;
    private final ProjectMemberRepository projectMemberRepository;
    private final MessageResponseDtoFactory messageResponseDtoFactory;

    @Transactional(readOnly = true)
    public List<ProjectDto> getProjects(Long userId) {

        isUserExistsById(userId);

        List<ProjectEntity> projects = projectRepository.findByOwnerUserId(userId).orElse(
                Collections.emptyList()
        );

        if (projects.isEmpty()) {
            return Collections.emptyList();
        }

        List<ProjectDto> projectsList = new ArrayList<>();

        for (ProjectEntity project : projects) {
            projectsList.add(projectDtoFactory.makeProjectDto(project));
        }

        return projectsList;
    }

    @Transactional
    public ProjectDto createProject(ProjectModificationRequest request, Long userId) {

        isUserExistsById(userId);

        isProjectNameUniqueness(request.getName());

        ProjectEntity project = projectEntityFactory.makeProjectEntity(request, userId);
        ProjectEntity savedProject = projectRepository.save(project);

        ProjectDto responseProjectDto = projectDtoFactory.makeProjectDto(savedProject);
        ProjectMemberEntity projectMemberEntity = projectMemberFactory
                .makeProjectMemberForManagerRole(responseProjectDto, userId);
        projectMemberRepository.save(projectMemberEntity);

        return responseProjectDto;
    }

    @Transactional
    public ProjectDto editProject(ProjectModificationRequest request, Long userId, Long projectId) {

        isUserExistsById(userId);
        isUserHaveRequiredRole(projectId, userId, MANAGER_ROLE);

        validateProjectModificationRequest(request);

        ProjectEntity project = isProjectExistsById(projectId);

        if (request.getName() != null) {
            isProjectNameUniqueness(request.getName());
            project.setName(request.getName());
        }

        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }

        ProjectEntity savedProject = projectRepository.save(project);

        return projectDtoFactory.makeProjectDto(savedProject);
    }

    @Transactional
    public MessageResponseDto deleteProject(Long userId, Long projectId) {

        isUserExistsById(userId);
        isUserHaveRequiredRole(projectId, userId, MANAGER_ROLE);

        ProjectEntity deletedProject = isProjectExistsById(projectId);

        projectRepository.deleteById(projectId);
        projectMemberRepository.deleteAllItemsByProjectId(projectId);

        return messageResponseDtoFactory.makeMessageResponseDto(
                "Project with name " + deletedProject.getName() + "was successfully deleted."
        );
    }

    @Transactional
    public MessageResponseDto inviteUserInProject(Long userId, Long projectId, String login) {

        isUserExistsById(userId);
        isUserHaveRequiredRole(projectId, userId, MANAGER_ROLE);

        UserEntity user = isUserExistsByLogin(login);

        isUserAlreadyInProject(user.getId(), userId, projectId);

        ProjectMemberEntity projectUsersRelationEntity = projectMemberFactory
                .makeProjectMemberForDeveloperRole(projectId, user.getId());

        projectMemberRepository.save(projectUsersRelationEntity);

        return messageResponseDtoFactory.makeMessageResponseDto(
                "User with login " + login + " was successfully invited in project."
        );
    }

    @Transactional(readOnly = true)
    public List<ProjectUsersDto> showAllUsersInProject(Long userId, Long projectId) {

        isUserExistsById(userId);
        isProjectExistsById(projectId);
        isUserHaveRequiredRole(projectId, userId, MANAGER_ROLE);

        return userRepository.findProjectUsersByProjectId(projectId);
    }

    @Transactional
    public ProjectMemberDto changeUserRole(Long userId, Long projectId, String login, String newRoleName) {

        isUserExistsById(userId);
        isProjectExistsById(projectId);
        isUserHaveRequiredRole(projectId, userId, MANAGER_ROLE);

        UserEntity user = isUserExistsByLogin(login);
        ProjectMemberEntity projectMemberEntity = projectMemberRepository.findByProjectIdAndUserId(projectId, user.getId())
                .orElseThrow(() -> new AppException("Project member doesn't exists", HttpStatus.NOT_FOUND)
        );

        switch (newRoleName) {
            case ("manager") -> projectMemberEntity.setProjectRole(ProjectRole.PROJECT_MANAGER_ROLE);
            case ("teamlead") -> projectMemberEntity.setProjectRole(ProjectRole.PROJECT_TEAM_LEAD_ROLE);
            case ("developer") -> projectMemberEntity.setProjectRole(ProjectRole.PROJECT_DEVELOPER_ROLE);
            default -> throw new AppException("Unexpected value for project role", HttpStatus.BAD_REQUEST);
        }

        ProjectMemberEntity savedProjectMember = projectMemberRepository.save(projectMemberEntity);

        return projectMemberDtoFactory.makeProjectMemberDto(savedProjectMember);
    }

    @Transactional
    public MessageResponseDto kickUserFromProject(Long userId, Long projectId, String login) {

        isUserExistsById(userId);
        isProjectExistsById(projectId);
        isUserHaveRequiredRole(projectId, userId, MANAGER_ROLE);

        UserEntity user = isUserExistsByLogin(login);
        projectMemberRepository.deleteById(user.getId());

        return messageResponseDtoFactory.makeMessageResponseDto(
                "User with login " + login + " was successfully kick from project"
        );
    }

    private UserEntity isUserExistsById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new AppException("User doesn't exists", HttpStatus.NOT_FOUND)
        );
    }

    private UserEntity isUserExistsByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(
                () -> new AppException("User doesn't exists with login " + login + ".", HttpStatus.NOT_FOUND)
        );
    }

    private void isProjectNameUniqueness(String projectName) {
        Optional<ProjectEntity> optionalProject = projectRepository.findByName(projectName);
        if (optionalProject.isPresent()) {
            throw new AppException(
                    "You can't rename this project because project with this name already exists", HttpStatus.BAD_REQUEST
            );
        }
    }

    private ProjectEntity isProjectExistsById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                () -> new AppException("Project not found", HttpStatus.NOT_FOUND)
        );
    }

    private void isUserHaveRequiredRole(Long projectId, Long userId, String roleName) {
        Optional<ProjectMemberEntity> optionalProjectMemberEntity = projectMemberRepository
                .findByProjectIdAndUserId(projectId, userId);
        ProjectMemberEntity projectMemberEntity = optionalProjectMemberEntity.orElseThrow(
                () -> new AppException("You don't have permission to access this action", HttpStatus.FORBIDDEN)
        );

        String userRole = projectMemberEntity.getProjectRole().toString();
        if (!userRole.equals(roleName)) {
            throw new AppException("You don't have permission to access this action", HttpStatus.FORBIDDEN);
        }
    }

    private void validateProjectModificationRequest(ProjectModificationRequest request) {
        if (request.getName() == null && request.getDescription() == null) {
            throw new AppException("Bad request", HttpStatus.BAD_REQUEST);
        }
    }

    private void isUserAlreadyInProject(Long checkingUserId, Long userId, Long projectId) {
        if (Objects.equals(checkingUserId, userId)) {
            throw new AppException("User already in project", HttpStatus.BAD_REQUEST);
        }

        Optional<ProjectMemberEntity> projectMemberEntity = projectMemberRepository
                .findByProjectIdAndUserId(projectId, checkingUserId);

        if (projectMemberEntity.isPresent()) {
            throw new AppException("User already in project", HttpStatus.BAD_REQUEST);
        }
    }
}
