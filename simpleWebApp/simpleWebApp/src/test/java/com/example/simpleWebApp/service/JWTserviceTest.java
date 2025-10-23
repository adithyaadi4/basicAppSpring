package com.example.simpleWebApp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JWTserviceTest {

    @InjectMocks
    private JWTService jwtService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JWTService();

        // Set secret and expiration manually (since @Value won't inject here)
        jwtService.getClass()
                .getDeclaredFields();

        // Using reflection to inject values
        try {
            var secretField = JWTService.class.getDeclaredField("jwtSecret");
            secretField.setAccessible(true);
            secretField.set(jwtService, "mysecretkeymysecretkeymysecretkey123");

            var expiryField = JWTService.class.getDeclaredField("jwtExpirationMs");
            expiryField.setAccessible(true);
            expiryField.set(jwtService, 100000L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        testUser = new User("testuser", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void testGenerateTokenAndExtractUsername() {
        String token = jwtService.generateToken(testUser);
        assertNotNull(token);

        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void testIsTokenValid_True() {
        String token = jwtService.generateToken(testUser);
        assertTrue(jwtService.isTokenValid(token, testUser));
    }

    @Test
    void testIsTokenValid_False_WhenDifferentUser() {
        String token = jwtService.generateToken(testUser);
        User anotherUser = new User("otheruser", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        assertFalse(jwtService.isTokenValid(token, anotherUser));
    }

    @Test
    void testExtractClaim_ReturnsCorrectSubject() {
        String token = jwtService.generateToken(testUser);
        String subject = jwtService.extractClaim(token, Claims::getSubject);
        assertEquals("testuser", subject);
    }

    @Test
    void testIsTokenExpired_ReturnsFalseForNewToken() {
        String token = jwtService.generateToken(testUser);
        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testTokenContainsRolesClaim() {
        String token = jwtService.generateToken(testUser);
        var roles = jwtService.extractClaim(token, claims -> claims.get("roles", java.util.List.class));
        assertTrue(roles.contains("ROLE_USER"));
    }
}
