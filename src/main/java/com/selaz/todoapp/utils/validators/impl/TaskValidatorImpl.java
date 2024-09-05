package com.selaz.todoapp.utils.validators.impl;

import com.selaz.todoapp.dtos.CreateTaskDTO;
import com.selaz.todoapp.dtos.UpdateTaskDTO;
import com.selaz.todoapp.utils.validators.TaskValidator;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskValidatorImpl implements TaskValidator {
    @Autowired
    Validator validator;

    @Override
    public void validateCreateTaskDTO(CreateTaskDTO createTaskDTO) {
        validateObject(createTaskDTO);
    }

    @Override
    public void validateUpdateTaskDTO(UpdateTaskDTO updateTaskDTO) {
        validateObject(updateTaskDTO);
    }

    private void validateObject(Object obj) {
        var errors = validator.validate(obj);
        if (!errors.isEmpty()) {
            throw new ConstraintViolationException(errors);
        }
    }
}
