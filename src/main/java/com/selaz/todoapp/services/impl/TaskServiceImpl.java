package com.selaz.todoapp.services.impl;

import com.selaz.todoapp.dtos.CreateTaskDTO;
import com.selaz.todoapp.dtos.UpdateTaskDTO;
import com.selaz.todoapp.entities.Status;
import com.selaz.todoapp.entities.Task;
import com.selaz.todoapp.entities.User;
import com.selaz.todoapp.exceptions.NotAllowedException;
import com.selaz.todoapp.exceptions.ResourceNotFoundException;
import com.selaz.todoapp.mappers.TaskMapper;
import com.selaz.todoapp.repositories.TaskRepository;
import com.selaz.todoapp.repositories.UserRepository;
import com.selaz.todoapp.services.TaskService;
import com.selaz.todoapp.utils.validators.TaskValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskValidator taskValidator;
    @Autowired
    TaskMapper taskMapper;

    @Override
    public List<Task> getUserTasks(Long userId, Status status, String sort) {
        Sort sortBy = sort == null ? Sort.unsorted() : Sort.by(sort);
        if (status != null) {
            return taskRepository.findByUser_IdAndStatus(userId, status, sortBy);
        }
        return taskRepository.findByUser_Id(userId, sortBy);
    }

    @Override
    public Task createTask(CreateTaskDTO createTaskDTO, Long userId) {
        taskValidator.validateCreateTaskDTO(createTaskDTO);

        User userRef = userRepository.getReferenceById(userId);
        Task task = new Task();

        taskMapper.taskFromCreateTaskDTO(createTaskDTO, task);
        task.setUser(userRef);

        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(UpdateTaskDTO updateTaskDTO, Long taskId, Long userId) {
        taskValidator.validateUpdateTaskDTO(updateTaskDTO);
        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isEmpty()) {
            throw new ResourceNotFoundException("Task whit this ID not found");
        }
        Task taskToUpdate = optionalTask.get();

        if (!Objects.equals(taskToUpdate.getUser().getId(), userId)) {
            throw new NotAllowedException("You cannot update other user task");
        }

        taskMapper.updateTaskFromUpdateTaskDTO(updateTaskDTO, taskToUpdate);

        return taskRepository.save(taskToUpdate);
    }

    @Override
    public void deleteTask(Long taskId, Long userId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            return;
        }

        Task task = optionalTask.get();
        if (!Objects.equals(task.getUser().getId(), userId)) {
            throw new NotAllowedException("You cannot delete other user task");
        }

        taskRepository.deleteById(task.getId());
    }
}
