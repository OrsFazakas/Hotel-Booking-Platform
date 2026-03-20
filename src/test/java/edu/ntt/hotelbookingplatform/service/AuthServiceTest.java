package edu.ntt.hotelbookingplatform.service;

import edu.ntt.hotelbookingplatform.exception.user.UserNotFoundException;
import edu.ntt.hotelbookingplatform.exception.user.UserWrongPasswordException;
import edu.ntt.hotelbookingplatform.model.Users;
import edu.ntt.hotelbookingplatform.service.interfaces.IJWTService;
import edu.ntt.hotelbookingplatform.service.interfaces.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private IUserService userService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private IJWTService jwtService;

    @InjectMocks
    private AuthService authService;

    private Users mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new Users();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("hashedPassword");
    }

    @Test
    void login_ValidCredentials_ReturnsToken() {
        when(userService.getUserByEmail("test@example.com")).thenReturn(mockUser);
        when(passwordEncoder.matches("rawPassword", "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken(mockUser)).thenReturn("jwt-token");

        String token = authService.login("test@example.com", "rawPassword");

        assertEquals("jwt-token", token);
        verify(userService).getUserByEmail("test@example.com");
        verify(passwordEncoder).matches("rawPassword", "hashedPassword");
        verify(jwtService).generateToken(mockUser);
    }

    @Test
    void login_WrongPassword_ThrowsUserWrongPasswordException() {
        String testEmail = "test@example.com";
        when(userService.getUserByEmail(testEmail)).thenReturn(mockUser);
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        assertThrows(UserWrongPasswordException.class,
                () -> authService.login(testEmail, "wrongPassword"));

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        String exceptionEmail = "unknown@example.com";
        when(userService.getUserByEmail(exceptionEmail))
                .thenThrow(new UserNotFoundException(exceptionEmail));

        assertThrows(UserNotFoundException.class,
                () -> authService.login(exceptionEmail, "anyPassword"));

        verify(passwordEncoder, never()).matches(any(), any());
    }
}