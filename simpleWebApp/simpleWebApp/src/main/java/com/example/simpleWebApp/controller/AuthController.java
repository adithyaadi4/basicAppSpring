package com.example.simpleWebApp.controller;
import com.example.simpleWebApp.config.JwtUtils;
import com.example.simpleWebApp.dto.AuthResponse;
import com.example.simpleWebApp.dto.LoginRequest;
import com.example.simpleWebApp.dto.RegisterRequest;
import com.example.simpleWebApp.model.User;
import com.example.simpleWebApp.service.AuthService;
import com.example.simpleWebApp.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
@Autowired
private JWTService jwtService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request.getUsername(), request.getPassword(), request.getRole());
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = authService.login(request.getUsername(), request.getPassword());
            String token = jwtService.generateToken(user);
            AuthResponse response = new AuthResponse(
                    "Login successful",
                    user.getUsername(),
                    user.getRole().name(),token
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(e.getMessage(), null, null,null));
        }
    }
}
