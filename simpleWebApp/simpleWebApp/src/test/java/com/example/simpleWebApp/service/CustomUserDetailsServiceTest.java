package com.example.simpleWebApp.service;

import com.example.simpleWebApp.model.User;
import com.example.simpleWebApp.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound_ReturnsUserDetails() {
        // Arrange
        User user = new User();
        user.setUsername("akash");
        user.setPassword("encodedPass");
        user.setRole(User.Role.ADMIN);
        user.setEnabled(true);

        when(userRepository.findByUsername("akash")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("akash");

        // Assert
        assertNotNull(userDetails);
        assertEquals("akash", userDetails.getUsername());
        assertEquals("encodedPass", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));

        verify(userRepository, times(1)).findByUsername("akash");
    }

    @Test
    void testLoadUserByUsername_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername("unknown")
        );

        assertEquals("User not found: unknown", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("unknown");
    }

    @Test
    void testLoadUserByUsername_DisabledUser_SetsDisabledFlag() {
        User user = new User();
        user.setUsername("inactive");
        user.setPassword("pass");
        user.setRole(User.Role.USER);
        user.setEnabled(false);

        when(userRepository.findByUsername("inactive")).thenReturn(Optional.of(user));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("inactive");
        assertNotNull(userDetails);
        assertFalse(userDetails.isEnabled());
        assertTrue(userDetails.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }
}
