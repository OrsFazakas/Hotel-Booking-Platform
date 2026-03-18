package edu.ntt.hotelbookingplatform.service;

import edu.ntt.hotelbookingplatform.exception.user.UserNotFoundException;
import edu.ntt.hotelbookingplatform.exception.user.UserWrongPasswordException;
import edu.ntt.hotelbookingplatform.model.Users;
import edu.ntt.hotelbookingplatform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Users testUser;
    private final String email = "test@example.com";

    @BeforeEach
    void setUp() {
        testUser = new Users(email, "encodedPassword", "John", "Doe", "Customer");
    }

    // 1. TESTING NAME UPDATE
    @Test
    void updateUserName_ShouldUpdateFields_WhenUserExists() {
        // Given
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // When
        userService.updateUserName(email, "James", "Smith");

        // Then
        assertEquals("James", testUser.getFirstName());
        assertEquals("Smith", testUser.getLastName());
        verify(userRepository, times(1)).save(testUser);
    }

    // 2. PASSWORD UPDATE - SUCCESS CASE
    @Test
    void updateUserPassword_ShouldEncodeAndSave_WhenOldPasswordMatches() {
        // Given
        String oldPass = "plainOld";
        String newPass = "plainNew";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(oldPass, testUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPass)).thenReturn("newEncodedHash");

        // When
        userService.updateUserPassword(email, oldPass, newPass);

        // Then
        assertEquals("newEncodedHash", testUser.getPassword());
        verify(userRepository, times(1)).save(testUser);
    }

    // 3. PASSWORD UPDATE - ERROR CASE (Wrong old password)
    @Test
    void updateUserPassword_ShouldThrowException_WhenOldPasswordIsWrong() {
        // Given
        String wrongOldPass = "wrongPassword";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(wrongOldPass, testUser.getPassword())).thenReturn(false);

        // When & Then
        assertThrows(UserWrongPasswordException.class, () ->
                userService.updateUserPassword(email, wrongOldPass, "anyNewPass")
        );
        verify(userRepository, never()).save(any());
    }

    // 4. ROLE UPDATE
    @Test
    void updateUserRole_ShouldChangeRole_WhenUserExists() {
        // Given
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // When
        userService.updateUserRole(email, "Admin");

        // Then
        assertEquals("Admin", testUser.getRole());
        verify(userRepository, times(1)).save(testUser);
    }

    // 5. DELETE - SUCCESS CASE
    @Test
    void deleteUser_ShouldCallDelete_WhenUserExists() {
        // Given
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // When
        userService.deleteUser(email);

        // Then
        verify(userRepository, times(1)).delete(testUser);
    }

    // 6. SEARCH - ERROR CASE (User does not exist)
    @Test
    void getUserByEmail_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        // Given
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () ->
                userService.getUserByEmail("nonexistent@test.com")
        );
    }
}