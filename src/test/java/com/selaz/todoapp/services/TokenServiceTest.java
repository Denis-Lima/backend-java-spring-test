package com.selaz.todoapp.services;

import com.selaz.todoapp.entities.User;
import com.selaz.todoapp.security.TokenService;
import com.selaz.todoapp.services.impl.AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TokenServiceTest {
    @Mock
    private HttpServletRequest request = new MockHttpServletRequest();
    @Autowired
    private TokenService tokenService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given user when generateToken, then the token is returned")
    void Given_User_When_GenerateToken_Then_TokenIsReturned() {
        User user = createTestUser();
        String token = tokenService.generateToken(user);

        Assertions.assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("Given correct token when validateToken, then the subject is returned")
    void Given_CorrectToken_When_ValidateToken_Then_SubjectIsReturned() {
        User user = createTestUser();
        String token = tokenService.generateToken(user);
        String subject = tokenService.validateToken(token);

        Assertions.assertThat(subject).isNotNull();
        Assertions.assertThat(subject).isEqualTo(user.getUsername());
    }

    @Test
    @DisplayName("Given incorrect token when validateToken, then throw Exception")
    void Given_IncorrectToken_When_ValidateToken_Then_ThrowException() {
        User user = createTestUser();
        String token = tokenService.generateToken(user);

        Assertions.assertThatThrownBy(() -> tokenService.validateToken(token + "incorrect"))
                .isNotNull();
    }

    @Test
    @DisplayName("Given token in header when getTokenFromRequest, then return the token")
    void Given_TokenInHeader_When_GetTokenFromRequest_Then_ReturnToken() {
        User user = createTestUser();
        String token = tokenService.generateToken(user);

        Mockito.when(request.getHeader(AuthorizationService.AUTH_HEADER_NAME)).thenReturn("Bearer " + token);

        Optional<String> optionalToken = TokenService.getTokenFromRequest(request);
        Assertions.assertThat(optionalToken).isNotEmpty();
        Assertions.assertThat(optionalToken.get()).isEqualTo(token);
    }

    @Test
    @DisplayName("Given missing header when getTokenFromRequest, then return empty token")
    void Given_MissingHeader_When_GetTokenFromRequest_Then_ReturnEmptyToken() {
        Mockito.when(request.getHeader(AuthorizationService.AUTH_HEADER_NAME)).thenReturn(null);

        Optional<String> optionalToken = TokenService.getTokenFromRequest(request);
        Assertions.assertThat(optionalToken).isEmpty();
    }

    @Test
    @DisplayName("Given missing bearer in header when getTokenFromRequest, then return empty token")
    void Given_MissingBearerInHeader_When_GetTokenFromRequest_Then_ReturnEmptyToken() {
        User user = createTestUser();
        String token = tokenService.generateToken(user);
        Mockito.when(request.getHeader(AuthorizationService.AUTH_HEADER_NAME)).thenReturn(token);

        Optional<String> optionalToken = TokenService.getTokenFromRequest(request);
        Assertions.assertThat(optionalToken).isEmpty();
    }

    @Test
    @DisplayName("Given missing token in header when getTokenFromRequest, then return empty token")
    void Given_MissingTokenInHeader_When_GetTokenFromRequest_Then_ReturnEmptyToken() {
        Mockito.when(request.getHeader(AuthorizationService.AUTH_HEADER_NAME)).thenReturn("Bearer");

        Optional<String> optionalToken = TokenService.getTokenFromRequest(request);
        Assertions.assertThat(optionalToken).isEmpty();
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
