package com.selaz.todoapp.utils.validators.impl;

import com.selaz.todoapp.dtos.UpdateUserDTO;
import com.selaz.todoapp.entities.User;
import com.selaz.todoapp.utils.validators.UserValidator;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserValidatorImpl implements UserValidator {
    @Autowired
    Validator validator;

    @Override
    public void validate(User user) throws ValidationException {
        validateObject(user);
        validateUserNivel(user.getNivel());
    }

    @Override
    public void validateUpdateUserDTO(UpdateUserDTO updateUserDTO) throws ValidationException {
        validateObject(updateUserDTO);
        if (updateUserDTO.nivel() != null) {
            validateUserNivel(updateUserDTO.nivel());
        }
    }

    private void validateUserNivel(String userNivel) throws ValidationException {
        String[] permittedNivel = {"admin", "user"};
        for (String nivel : permittedNivel) {
            if (nivel.equals(userNivel)) {
                return;
            }
        }

        throw new ValidationException("User nivel should be one of %s. Got '%s'".formatted(Arrays.toString(permittedNivel), userNivel));
    }

    private void validateObject(Object obj) {
        var errors = validator.validate(obj);
        if (!errors.isEmpty()) {
            throw new ConstraintViolationException(errors);
        }
    }
}
