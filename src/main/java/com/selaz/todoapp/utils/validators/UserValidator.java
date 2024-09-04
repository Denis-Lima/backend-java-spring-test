package com.selaz.todoapp.utils.validators;

import com.selaz.todoapp.entities.User;
import jakarta.xml.bind.ValidationException;

public interface UserValidator {
    void validate(User user) throws ValidationException;
}
