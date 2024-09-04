package com.selaz.todoapp.utils.validators.impl;

import com.selaz.todoapp.entities.User;
import com.selaz.todoapp.utils.validators.UserValidator;
import jakarta.xml.bind.ValidationException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserValidatorImpl implements UserValidator {


    @Override
    public void validate(User user) throws ValidationException {
        // others fields is already validated by annotations
        validateUserNivel(user);
    }

    private void validateUserNivel(User user) throws ValidationException {
        String userNivel = user.getNivel();
        String[] permittedNivel = {"admin", "user"};
        for (String nivel : permittedNivel) {
            if (nivel.equals(userNivel)) {
                return;
            }
        }

        throw new ValidationException("User nivel should be one of %s. Got '%s'".formatted(Arrays.toString(permittedNivel), userNivel));
    }
}
