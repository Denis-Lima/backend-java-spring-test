package com.selaz.todoapp.services;

import com.selaz.todoapp.dtos.CreateUserDTO;
import com.selaz.todoapp.dtos.UpdateUserDTO;
import com.selaz.todoapp.entities.User;
import jakarta.xml.bind.ValidationException;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();

    User createUser(CreateUserDTO createUserInfo) throws ValidationException;

    User updateUser(Long userId, UpdateUserDTO updateUserInfo) throws ValidationException;

    void deleteUser(Long userId);
}
