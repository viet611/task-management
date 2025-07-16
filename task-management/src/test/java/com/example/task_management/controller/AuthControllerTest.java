package com.example.task_management.controller;

import com.example.task_management.dto.ApiResponse;
import com.example.task_management.dto.LoginRequest;
import com.example.task_management.dto.RegisterRequest;
import com.example.task_management.entity.User;
import com.example.task_management.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setRole("USER");

        User createdUser = new User();
        createdUser.setId(1);
        createdUser.setUsername("testuser");
        createdUser.setRole("USER");

        when(userService.register(any(User.class))).thenReturn(createdUser);

        ApiResponse<User> response = authController.register(request);

        assertTrue(response.isSuccess());
        assertEquals("User registered successfully", response.getMessage());
        assertEquals("testuser", response.getData().getUsername());

        verify(userService, times(1)).register(any(User.class));
    }

    @Test
    void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        String mockToken = "mock-jwt-token";

        when(userService.login(eq("testuser"), eq("password123"))).thenReturn(mockToken);

        ApiResponse<String> response = authController.login(loginRequest);

        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
        assertEquals(mockToken, response.getData());

        verify(userService, times(1)).login("testuser", "password123");
    }
}
