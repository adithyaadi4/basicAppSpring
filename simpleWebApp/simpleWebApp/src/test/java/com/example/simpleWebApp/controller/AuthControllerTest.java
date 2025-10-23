package com.example.simpleWebApp.controller;

import com.example.simpleWebApp.dto.AuthResponse;
import com.example.simpleWebApp.dto.LoginRequest;
import com.example.simpleWebApp.dto.RegisterRequest;
import com.example.simpleWebApp.model.User;
import com.example.simpleWebApp.service.AuthService;
import com.example.simpleWebApp.service.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;
    private AuthService authService;
    private JWTService jwtService;
    private AuthController authController;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        jwtService = mock(JWTService.class);

        authController = new AuthController();
        ReflectionTestUtils.setField(authController, "authService", authService);
        ReflectionTestUtils.setField(authController, "jwtService", jwtService);

        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testUser");
        request.setPassword("pass123");
        request.setRole("USER");

        User user = new User();
        user.setUsername("testUser");
        user.setRole(User.Role.USER);

        when(authService.register(anyString(), anyString(), anyString())).thenReturn(user);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));

        verify(authService, times(1)).register("testUser", "pass123", "USER");
    }

    @Test
    void testRegisterFailure() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testUser");
        request.setPassword("pass123");
        request.setRole("USER");

        when(authService.register(anyString(), anyString(), anyString())).thenThrow(new RuntimeException());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Registration failed"));

        verify(authService, times(1)).register("testUser", "pass123", "USER");
    }

    @Test
    void testLoginSuccess() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("testUser");
        request.setPassword("pass123");

        User user = new User();
        user.setUsername("testUser");
        user.setRole(User.Role.USER);

        when(authService.login(anyString(), anyString())).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("mocked-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.token").value("mocked-token"));

        verify(authService, times(1)).login("testUser", "pass123");
        verify(jwtService, times(1)).generateToken(user);
    }

    @Test
    void testLoginFailure() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("testUser");
        request.setPassword("wrongPass");

        when(authService.login(anyString(), anyString())).thenThrow(new RuntimeException("Invalid username or password"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid username or password"))
                .andExpect(jsonPath("$.username").isEmpty())
                .andExpect(jsonPath("$.role").isEmpty())
                .andExpect(jsonPath("$.token").isEmpty());

        verify(authService, times(1)).login("testUser", "wrongPass");
        verify(jwtService, never()).generateToken(any());
    }
}
