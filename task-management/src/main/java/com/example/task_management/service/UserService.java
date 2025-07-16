package com.example.task_management.service;

import com.example.task_management.dto.RegisterRequest;
import com.example.task_management.entity.User;
import com.example.task_management.repository.UserRepository;
import com.example.task_management.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public User register(User user) {
        logger.info("Attempting to register user: {}", user.getUsername());
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.warn("Registration failed. Username {} already exists", user.getUsername());
            throw new RuntimeException("Username already exists");
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with id: {}", savedUser.getId());
        return savedUser;
    }

    public String login(String username, String password) {
        logger.info("User login attempt for username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Login failed. Username {} not found", username);
                    return new RuntimeException("Invalid username or password");
                });

        if (passwordEncoder.matches(password, user.getPasswordHash())) {
            String token = jwtUtil.generateToken(username);
            logger.info("User {} logged in successfully", username);
            return token;
        }
        logger.warn("Login failed for username {}: incorrect password", username);
        throw new RuntimeException("Invalid username or password");
    }

    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        logger.info("Fetching user by id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: {}", id);
                    return new RuntimeException("User not found");
                });
    }

    public User updateUserFromRequest(int id, RegisterRequest request) {
        logger.info("Updating user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: {}", id);
                    return new RuntimeException("User not found with id: " + id);
                });

        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        User updatedUser = userRepository.save(user);
        logger.info("User with id {} updated successfully", id);
        return updatedUser;
    }

    public void deleteUser(int id) {
        logger.info("Deleting user with id: {}", id);
        User existingUser = getUserById(id);
        userRepository.delete(existingUser);
        logger.info("User with id {} deleted successfully", id);
    }
}
