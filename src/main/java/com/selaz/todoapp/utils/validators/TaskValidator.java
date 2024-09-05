package com.selaz.todoapp.utils.validators;

import com.selaz.todoapp.dtos.CreateTaskDTO;
import com.selaz.todoapp.dtos.UpdateTaskDTO;

public interface TaskValidator {
    void validateCreateTaskDTO(CreateTaskDTO createTaskDTO);

    void validateUpdateTaskDTO(UpdateTaskDTO updateTaskDTO);

}
