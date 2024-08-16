package org.schizoscript.backend.services;

import lombok.RequiredArgsConstructor;
import org.schizoscript.backend.dtos.task.CreateTaskRequestDto;
import org.schizoscript.backend.dtos.task.TaskDto;
import org.schizoscript.backend.exceptions.AppException;
import org.schizoscript.backend.factories.TaskDtoFactory;
import org.schizoscript.backend.factories.TaskEntityFactory;
import org.schizoscript.backend.storage.entities.ProjectEntity;
import org.schizoscript.backend.storage.entities.TaskEntity;
import org.schizoscript.backend.storage.entities.UserEntity;
import org.schizoscript.backend.storage.repositories.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public TaskDto showTaskById(Long userId, Long projectId, Long taskId) {

        helperService.isUserMemberOfProject(userId, projectId);
        helperService.isProjectExistsById(projectId);

        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
                () -> new AppException("Task doesn't exists", HttpStatus.BAD_REQUEST)
        );

        return taskDtoFactory.makeTaskDto(task);
    }

    public List<TaskDto> getAllTasks(Long userId, Long projectId) {

        helperService.isUserExistsById(userId);
        helperService.isProjectExistsById(projectId);
        helperService.isUserMemberOfProject(userId, projectId);

        List<TaskEntity> tasksList = taskRepository.findByProjectId(projectId).orElse(
                Collections.emptyList()
        );

        if (tasksList.isEmpty()) {
            return Collections.emptyList();
        }

        List<TaskDto> taskDtoList = new ArrayList<>();

        for (TaskEntity taskEntity : tasksList) {
            taskDtoList.add(taskDtoFactory.makeTaskDto(taskEntity));
        }

        return taskDtoList;
    }
}
