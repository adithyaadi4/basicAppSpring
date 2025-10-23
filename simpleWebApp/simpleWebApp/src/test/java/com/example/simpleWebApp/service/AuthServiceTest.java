package com.example.simpleWebApp.service;

import com.example.simpleWebApp.model.User;
import com.example.simpleWebApp.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private JWTService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_NewUser_Success() {
        when(userRepository.findByUsername("akash")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = authService.register("akash", "pass", "ADMIN");

        assertNotNull(result);
        assertEquals("akash", result.getUsername());
        assertEquals(User.Role.ADMIN, result.getRole());
        assertTrue(result.isEnabled());
    }

    @Test
    void testRegister_ExistingUser_ThrowsException() {
        when(userRepository.findByUsername("akash")).thenReturn(Optional.of(new User()));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.register("akash", "pass", "USER"));

        assertEquals("Username already exists", ex.getMessage());
    }

    @Test
    void testRegister_InvalidRole_ThrowsException() {
        when(userRepository.findByUsername("akash")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.register("akash", "pass", "INVALID"));

        assertEquals("Invalid role. Must be USER or ADMIN", ex.getMessage());
    }

    @Test
    void testRegister_NullRole_DefaultsToUser() {
        when(userRepository.findByUsername("akash")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = authService.register("akash", "pass", null);

        assertEquals(User.Role.USER, result.getRole());
    }

    // ---------- login() TESTS ----------

    @Test
    void testLogin_ValidCredentials_Success() {
        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);

        User user = new User();
        user.setUsername("akash");
        when(userRepository.findByUsername("akash")).thenReturn(Optional.of(user));

        User result = authService.login("akash", "password");
        assertEquals("akash", result.getUsername());
    }

    @Test
    void testLogin_InvalidCredentials_ThrowsException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(mock(AuthenticationException.class));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login("akash", "wrong"));

        assertEquals("Invalid username or password", ex.getMessage());
    }

    @Test
    void testLogin_UserNotFound_ThrowsException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userRepository.findByUsername("akash")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login("akash", "password"));

        assertEquals("User not found", ex.getMessage());
    }
}
