package com.flatproof.controller;

import com.flatproof.dto.auth.AuthResponse;
import com.flatproof.dto.auth.LoginRequest;
import com.flatproof.dto.auth.RegisterRequest;
import com.flatproof.entity.User;
import com.flatproof.security.JwtService;
import com.flatproof.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token,"User registered successfully");
    }
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request);
        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, "Login successfully");
    }
}
