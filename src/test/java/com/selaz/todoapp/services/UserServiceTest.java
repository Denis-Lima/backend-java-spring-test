package com.selaz.todoapp.services;

import com.selaz.todoapp.dtos.CreateUserDTO;
import com.selaz.todoapp.dtos.UpdateUserDTO;
import com.selaz.todoapp.entities.User;
import com.selaz.todoapp.exceptions.ResourceAlreadyExistsException;
import com.selaz.todoapp.repositories.UserRepository;
import com.selaz.todoapp.services.impl.UserServiceImpl;
import jakarta.validation.ConstraintViolationException;
import jakarta.xml.bind.ValidationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {
    private final String PASSWORD = "testPassword";
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Autowired
    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn(PASSWORD);
    }

    @Test
    @DisplayName("Given correct info when createUser, then the user should be created and returned")
    void Given_CorrectInfo_When_CreateUser_Then_UserIsCreatedAndReturned() throws ValidationException {
        CreateUserDTO createUserDTO = new CreateUserDTO("testName", "admin", PASSWORD);

        User user = getUserFromCreateUserDTO(createUserDTO);
        Mockito.when(userRepository.findByUsernameIgnoreCase(createUserDTO.username())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        User createdUser = userService.createUser(createUserDTO);
        Assertions.assertThat(createdUser).isNotNull();
        Assertions.assertThat(createdUser).isSameAs(user);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Given existing username when createUser, then throw ResourceAlreadyExistsException")
    void Given_ExistingUsername_When_CreateUser_Then_ThrowResourceAlreadyExists() {
        CreateUserDTO createUserDTO = new CreateUserDTO("testName", "admin", PASSWORD);
        User user = getUserFromCreateUserDTO(createUserDTO);
        Mockito.when(userRepository.findByUsernameIgnoreCase(createUserDTO.username())).thenReturn(Optional.of(user));

        Assertions.assertThatThrownBy(() -> userService.createUser(createUserDTO))
                .isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    @DisplayName("Given invalid nivel when createUser, then throw ValidationException")
    void Given_InvalidNivel_When_CreateUser_Then_ThrowValidationException() {
        CreateUserDTO createUserDTO = new CreateUserDTO("testName", "now allowed", PASSWORD);
        Mockito.when(userRepository.findByUsernameIgnoreCase(createUserDTO.username())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.createUser(createUserDTO))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("Given invalid field when createUser, then throw ConstraintViolationException")
    void Given_InvalidField_When_CreateUser_Then_ThrowConstraintViolation() {
        CreateUserDTO createUserDTO = new CreateUserDTO("", "now allowed", PASSWORD);
        Mockito.when(userRepository.findByUsernameIgnoreCase(createUserDTO.username())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.createUser(createUserDTO))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Given no users created when findAllUsers, then return empty list")
    void Given_NoUsersCreated_When_FindAllUsers_Then_ReturnEmptyList() {
        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> users = userService.findAllUsers();
        Assertions.assertThat(users).isEmpty();
    }

    @Test
    @DisplayName("Given that has users created when findAllUsers, then return users in list")
    void Given_HasUsersCreated_When_FindAllUsers_Then_ReturnUsersInList() {
        User user1 = createTestUser(1L);
        User user2 = createTestUser(2L);
        User user3 = createTestUser(3L);
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user1, user2, user3));

        List<User> users = userService.findAllUsers();
        Assertions.assertThat(users).size().isEqualTo(3);
        Assertions.assertThat(users).contains(user1, user2, user3);
    }

    @Test
    @DisplayName("Given correct info when updateUser, then the updated user should be updated and returned")
    void Given_CorrectInfo_When_UpdateUser_Then_ReturnUpdatedUser() throws ValidationException {
        User user = createTestUser(1L);
        UpdateUserDTO updateUserDTO = new UpdateUserDTO("newName", "user", "newPassword");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUsernameIgnoreCase(updateUserDTO.username())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn(updateUserDTO.password());

        User updatedUser = userService.updateUser(user.getId(), updateUserDTO);

        Assertions.assertThat(updatedUser).isNotNull();
        Assertions.assertThat(updatedUser.getId()).isEqualTo(user.getId());
        Assertions.assertThat(updatedUser.getNivel()).isEqualTo(updateUserDTO.nivel());
        Assertions.assertThat(updatedUser.getPassword()).isEqualTo(updateUserDTO.password());
        Assertions.assertThat(updatedUser.getUsername()).isEqualTo(updateUserDTO.username());

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Given existing username when updateUser, then throw ResourceAlreadyExistsException")
    void Given_ExistingUsername_When_UpdateUser_Then_ThrowResourceAlreadyExists() {
        User userToUpdate = createTestUser(1L);
        User existingUser = createTestUser(2L);
        UpdateUserDTO updateUserDTO = new UpdateUserDTO(existingUser.getUsername(), null, null);

        Mockito.when(userRepository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        Mockito.when(userRepository.findByUsernameIgnoreCase(updateUserDTO.username()))
                .thenReturn(Optional.of(existingUser));

        Assertions.assertThatThrownBy(() -> userService.updateUser(userToUpdate.getId(), updateUserDTO))
                .isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    @DisplayName("Given invalid nivel when updateUser, then throw ValidationException")
    void Given_InvalidNivel_When_UpdateUser_Then_ThrowValidationException() {
        User userToUpdate = createTestUser(1L);
        UpdateUserDTO updateUserDTO = new UpdateUserDTO(null, "not allowed", null);

        Mockito.when(userRepository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        Mockito.when(userRepository.findByUsernameIgnoreCase(updateUserDTO.username()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.updateUser(userToUpdate.getId(), updateUserDTO))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("Given invalid field when updateUser, then throw ConstraintViolationException")
    void Given_InvalidField_When_UpdateUser_Then_ThrowConstraintViolation() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO("", "admin", null);

        Assertions.assertThatThrownBy(() -> userService.updateUser(1L, updateUserDTO))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Given existing user ID when deleteUser, then user is deleted")
    void Given_ExistingUserId_When_DeleteUser_Then_UserIsDeleted() {
        User user = createTestUser(1L);
        Mockito.doNothing().when(userRepository).deleteById(user.getId());

        userService.deleteUser(user.getId());
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(user.getId());
    }

    private User getUserFromCreateUserDTO(CreateUserDTO createUserDTO) {
        User user = new User();
        user.setId(1L);
        user.setNivel(createUserDTO.nivel());
        user.setPassword(createUserDTO.password());
        user.setUsername(createUserDTO.username());
        return user;
    }

    private User createTestUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setNivel("admin");
        user.setPassword(PASSWORD);
        user.setUsername("testUser" + id);
        return user;
    }
}
