package com.example.task_management.controller;

import com.example.task_management.dto.ApiResponse;
import com.example.task_management.dto.LoginRequest;
import com.example.task_management.dto.RegisterRequest;
import com.example.task_management.entity.User;
import com.example.task_management.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody @Valid RegisterRequest registerRequest) {
        logger.info("Register API called for username: {}", registerRequest.getUsername());

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPasswordHash(registerRequest.getPassword());
        user.setRole(registerRequest.getRole());

        User createdUser = userService.register(user);

        logger.info("User registered successfully with id: {}", createdUser.getId());
        return new ApiResponse<>(true, "User registered successfully", createdUser);
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody @Valid LoginRequest request) {
        logger.info("Login API called for username: {}", request.getUsername());

        String token = userService.login(request.getUsername(), request.getPassword());

        logger.info("User {} logged in successfully", request.getUsername());
        return new ApiResponse<>(true, "Login successful", token);
    }
}
