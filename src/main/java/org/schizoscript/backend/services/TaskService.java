package org.schizoscript.backend.services;

import lombok.RequiredArgsConstructor;
import org.schizoscript.backend.dtos.task.CreateTaskRequestDto;
import org.schizoscript.backend.dtos.task.TaskDto;
import org.schizoscript.backend.factories.TaskDtoFactory;
import org.schizoscript.backend.factories.TaskEntityFactory;
import org.schizoscript.backend.storage.entities.ProjectEntity;
import org.schizoscript.backend.storage.entities.TaskEntity;
import org.schizoscript.backend.storage.entities.UserEntity;
import org.schizoscript.backend.storage.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final HelperService helperService;
    private final TaskRepository taskRepository;
    private final TaskDtoFactory taskDtoFactory;
    private final TaskEntityFactory taskEntityFactory;

    @Transactional
    public TaskDto createTask(Long userId, Long projectId, CreateTaskRequestDto requestDto) {

        UserEntity user = helperService.isUserExistsById(userId);
        ProjectEntity project = helperService.isProjectExistsById(projectId);
        helperService.isUserHaveRequiredRole(
                userId, projectId, new String[] {"PROJECT_MANAGER_ROLE", "PROJECT_TEAM_LEAD_ROLE"}
        );
        helperService.isUserMemberOfProject(requestDto.getIssuedId(), projectId);

        helperService.isTaskPriorityValid(requestDto.getPriority());

        TaskEntity task = taskEntityFactory.makeTaskEntity(requestDto, userId, projectId);
        TaskEntity savedTask = taskRepository.save(task);

        return taskDtoFactory.makeTaskDto(savedTask);
    }
}
