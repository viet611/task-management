package com.example.task_management.controller;

import com.example.task_management.dto.ApiResponse;
import com.example.task_management.dto.RegisterRequest;
import com.example.task_management.entity.User;
import com.example.task_management.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        ApiResponse<List<User>> response = userController.getAllUsers();

        assertTrue(response.isSuccess());
        assertEquals(2, response.getData().size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");

        when(userService.getUserById(1)).thenReturn(user);

        ApiResponse<User> response = userController.getUserById(1);

        assertTrue(response.isSuccess());
        assertEquals("testuser", response.getData().getUsername());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void testCreateUser() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("password");
        request.setRole("USER");

        User createdUser = new User();
        createdUser.setId(1);
        createdUser.setUsername("newuser");

        when(userService.register(any(User.class))).thenReturn(createdUser);

        ApiResponse<User> response = userController.createUser(request);

        assertTrue(response.isSuccess());
        assertEquals("User created successfully", response.getMessage());
        assertEquals("newuser", response.getData().getUsername());
        verify(userService, times(1)).register(any(User.class));
    }

    @Test
    void testUpdateUser() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("updatedUser");
        request.setPassword("newpass");
        request.setRole("ADMIN");

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setUsername("updatedUser");

        when(userService.updateUserFromRequest(eq(1), any(RegisterRequest.class))).thenReturn(updatedUser);

        ApiResponse<User> response = userController.updateUser(1, request);

        assertTrue(response.isSuccess());
        assertEquals("User updated successfully", response.getMessage());
        assertEquals("updatedUser", response.getData().getUsername());
        verify(userService, times(1)).updateUserFromRequest(eq(1), any(RegisterRequest.class));
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).deleteUser(1);

        ApiResponse<String> response = userController.deleteUser(1);

        assertTrue(response.isSuccess());
        assertEquals("User deleted successfully", response.getMessage());
        verify(userService, times(1)).deleteUser(1);
    }
}
