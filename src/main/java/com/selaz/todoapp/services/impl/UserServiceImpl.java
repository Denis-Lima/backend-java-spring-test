package com.selaz.todoapp.services.impl;

import com.selaz.todoapp.dtos.CreateUserDTO;
import com.selaz.todoapp.dtos.UpdateUserDTO;
import com.selaz.todoapp.entities.User;
import com.selaz.todoapp.exceptions.ResourceAlreadyExistsException;
import com.selaz.todoapp.exceptions.ResourceNotFoundException;
import com.selaz.todoapp.mappers.UserMapper;
import com.selaz.todoapp.repositories.UserRepository;
import com.selaz.todoapp.services.UserService;
import com.selaz.todoapp.utils.validators.UserValidator;
import jakarta.xml.bind.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserValidator userValidator;


    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(CreateUserDTO createUserInfo) throws ValidationException {
        checkUsernameExists(createUserInfo.username());

        User newUser = new User();
        userMapper.userFromCreateUserDTO(createUserInfo, newUser);
        newUser.setPassword(passwordEncoder.encode(createUserInfo.password()));

        userValidator.validate(newUser);

        return userRepository.save(newUser);
    }

    private void checkUsernameExists(String username) {
        Optional<User> optionalUser = userRepository.findByUsernameIgnoreCase(username);
        if (optionalUser.isPresent()) {
            throw new ResourceAlreadyExistsException("This username is already used");
        }
    }

    @Override
    public User updateUser(Long userId, UpdateUserDTO updateUserInfo) throws ValidationException {
        userValidator.validateUpdateUserDTO(updateUserInfo);

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User with this ID not found");
        }

        if (updateUserInfo.username() != null) {
            checkUsernameExists(updateUserInfo.username());
        }

        User user = optionalUser.get();
        userMapper.updateUserFromUpdateUserDTO(updateUserInfo, user);

        if (updateUserInfo.password() != null) {
            user.setPassword(passwordEncoder.encode(updateUserInfo.password()));
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
