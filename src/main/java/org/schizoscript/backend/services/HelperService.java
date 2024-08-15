package org.schizoscript.backend.services;

import lombok.RequiredArgsConstructor;
import org.schizoscript.backend.exceptions.AppException;
import org.schizoscript.backend.storage.entities.ProjectEntity;
import org.schizoscript.backend.storage.entities.ProjectMemberEntity;
import org.schizoscript.backend.storage.entities.UserEntity;
import org.schizoscript.backend.storage.repositories.ProjectMemberRepository;
import org.schizoscript.backend.storage.repositories.ProjectRepository;
import org.schizoscript.backend.storage.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class HelperService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public UserEntity isUserExistsById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new AppException("User doesn't exists", HttpStatus.NOT_FOUND)
        );
    }

    public ProjectEntity isProjectExistsById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                () -> new AppException("Project not found", HttpStatus.NOT_FOUND)
        );
    }

    public void isUserHaveRequiredRole(Long userId, Long projectId, String[] rolesArray) {
        Optional<ProjectMemberEntity> optionalProjectMemberEntity = projectMemberRepository
                .findByProjectIdAndUserId(projectId, userId);
        ProjectMemberEntity projectMemberEntity = optionalProjectMemberEntity.orElseThrow(
                () -> new AppException("You don't have permission to access this action", HttpStatus.FORBIDDEN)
        );

        String userRole = projectMemberEntity.getProjectRole().toString();

        boolean hasRequiredRole = Stream.of(rolesArray).anyMatch(userRole::equals);

        if (!hasRequiredRole) {
            throw new AppException("You don't have permission to access this action", HttpStatus.FORBIDDEN);
        }
    }

    public void isTaskPriorityValid(String taskPriority) {

        String[] prioritiesNameList = new String[] {"low", "medium", "high"};

        boolean hasValidPriorityName = Stream.of(prioritiesNameList).anyMatch(taskPriority.toLowerCase()::equals);

        if (!hasValidPriorityName) {
            throw new AppException("The name for the priority is not valid", HttpStatus.BAD_REQUEST);
        }
    }

    public void isUserMemberOfProject(Long userId, Long projectId) {

        Optional<ProjectMemberEntity> optionalProjectMember = projectMemberRepository
                .findByProjectIdAndUserId(projectId, userId);

        if (optionalProjectMember.isEmpty()) {
            throw new AppException("User with " + userId + " id is not member of project", HttpStatus.BAD_REQUEST);
        }
    }
}
