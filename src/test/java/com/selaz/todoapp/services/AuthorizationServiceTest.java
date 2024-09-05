package com.selaz.todoapp.services;

import com.selaz.todoapp.entities.User;
import com.selaz.todoapp.repositories.UserRepository;
import com.selaz.todoapp.services.impl.AuthorizationService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthorizationServiceTest {
    @Mock
    UserRepository userRepository;
    @Autowired
    @InjectMocks
    AuthorizationService authorizationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("given correct username when loadUserByUsername, then return UserDetails")
    void Given_CorrectUsername_When_LoadUserByUsername_Then_ReturnUserDetails() {
        User user = createTestUser();

        Mockito.when(userRepository.findByUsernameIgnoreCase(user.getUsername())).thenReturn(Optional.of(user));

        Assertions.assertThat(authorizationService.loadUserByUsername(user.getUsername()))
                .isInstanceOf(UserDetails.class);
    }

    @Test
    @DisplayName("given not existing username when loadUserByUsername, then throw UsernameNotFoundException")
    void Given_NotExistingUsername_When_LoadUserByUsername_Then_ThrowUsernameNotFoundException() {
        Mockito.when(userRepository.findByUsernameIgnoreCase(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> authorizationService.loadUserByUsername("username"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setNivel("admin");
        user.setPassword("PASSWORD");
        user.setUsername("testUser" + (Long) 1L);
        return user;
    }
}
