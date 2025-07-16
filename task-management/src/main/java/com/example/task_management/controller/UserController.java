package com.example.task_management.controller;

import com.example.task_management.dto.ApiResponse;
import com.example.task_management.dto.RegisterRequest;
import com.example.task_management.entity.User;
import com.example.task_management.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = userService.getAllUsers();
        logger.info("Fetched {} users", users.size());
        return new ApiResponse<>(true, "Fetched all users", users);
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable int id) {
        logger.info("Fetching user with id: {}", id);
        User user = userService.getUserById(id);
        logger.info("Fetched user with id: {}", id);
        return new ApiResponse<>(true, "Fetched user by ID", user);
    }

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody @Valid RegisterRequest registerRequest) {
        logger.info("Creating user with username: {}", registerRequest.getUsername());
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPasswordHash(registerRequest.getPassword());
        user.setRole(registerRequest.getRole());

        User createdUser = userService.register(user);
        logger.info("User created successfully with id: {}", createdUser.getId());
        return new ApiResponse<>(true, "User created successfully", createdUser);
    }

    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(@PathVariable int id, @RequestBody @Valid RegisterRequest registerRequest) {
        logger.info("Updating user with id: {}", id);
        User updatedUser = userService.updateUserFromRequest(id, registerRequest);
        logger.info("User with id: {} updated successfully", id);
        return new ApiResponse<>(true, "User updated successfully", updatedUser);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable int id) {
        logger.info("Deleting user with id: {}", id);
        userService.deleteUser(id);
        logger.info("User with id: {} deleted successfully", id);
        return new ApiResponse<>(true, "User deleted successfully", null);
    }
}
