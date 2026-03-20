package edu.ntt.hotelbookingplatform.service;

import edu.ntt.hotelbookingplatform.controller.AuthController;
import edu.ntt.hotelbookingplatform.dto.in.LoginDTO;
import edu.ntt.hotelbookingplatform.dto.in.UserCreationDTO;
import edu.ntt.hotelbookingplatform.dto.mapper.UserMapper;
import edu.ntt.hotelbookingplatform.exception.user.UserAlreadyExistsException;
import edu.ntt.hotelbookingplatform.exception.user.UserWrongPasswordException;
import edu.ntt.hotelbookingplatform.model.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_ValidCredentials_ReturnsToken() {
        String testEmail = "test@example.com";
        String testPassword = "password";

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(testEmail);
        loginDTO.setPassword(testPassword);

        when(authService.login(testEmail, testPassword)).thenReturn("jwt-token");

        String token = authController.login(loginDTO);

        assertEquals("jwt-token", token);
        verify(authService).login(testEmail, testPassword);
    }

    @Test
    void login_ServiceThrowsException_PropagatesException() {
        String testEmail = "test@example.com";

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(testEmail);
        loginDTO.setPassword("wrongPassword");

        when(authService.login(any(), any())).thenThrow(new UserWrongPasswordException());

        assertThrows(UserWrongPasswordException.class, () -> authController.login(loginDTO));
    }

    @Test
    void register_ValidUser_ReturnsOkResponse() {
        UserCreationDTO userCreationDTO = new UserCreationDTO();
        Users mappedUser = new Users();

        when(userMapper.toUser(userCreationDTO)).thenReturn(mappedUser);
        doNothing().when(userService).createUser(mappedUser);

        ResponseEntity<String> response = authController.register(userCreationDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Successfully registered!", response.getBody());
        verify(userMapper).toUser(userCreationDTO);
        verify(userService).createUser(mappedUser);
    }

    @Test
    void register_ServiceThrowsException_PropagatesException() {
        UserCreationDTO userCreationDTO = new UserCreationDTO();
        Users mappedUser = new Users();
        String testEmail = mappedUser.getEmail();

        when(userMapper.toUser(userCreationDTO)).thenReturn(mappedUser);

        doThrow(new UserAlreadyExistsException(testEmail)).when(userService).createUser(mappedUser);

        assertThrows(RuntimeException.class, () -> authController.register(userCreationDTO));
    }
}