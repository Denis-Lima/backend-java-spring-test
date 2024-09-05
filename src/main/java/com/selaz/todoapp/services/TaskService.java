package com.selaz.todoapp.services;

import com.selaz.todoapp.dtos.CreateTaskDTO;
import com.selaz.todoapp.dtos.UpdateTaskDTO;
import com.selaz.todoapp.entities.Status;
import com.selaz.todoapp.entities.Task;

import java.util.List;

public interface TaskService {
    List<Task> getUserTasks(Long userId, Status status, String sort);

    Task createTask(CreateTaskDTO createTaskDTO, Long userId);

    Task updateTask(UpdateTaskDTO updateTaskDTO, Long taskId, Long userId);

    void deleteTask(Long taskId, Long userId);
}
