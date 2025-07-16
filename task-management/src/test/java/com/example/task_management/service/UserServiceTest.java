package com.example.task_management.service;

import com.example.task_management.entity.User;
import com.example.task_management.repository.UserRepository;
import com.example.task_management.security.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegister() {
        User user = new User();
        user.setUsername("testuser");
        user.setPasswordHash("rawpassword");

        Mockito.when(passwordEncoder.encode("rawpassword")).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User registeredUser = userService.register(user);

        Assertions.assertEquals("encodedPassword", registeredUser.getPasswordHash());
    }

    @Test
    void testLoginSuccess() {
        String username = "testuser";
        String rawPassword = "password";
        String encodedPassword = "encodedPassword";
        String token = "jwtToken";

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(encodedPassword);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        Mockito.when(jwtUtil.generateToken(username)).thenReturn(token);

        String generatedToken = userService.login(username, rawPassword);

        Assertions.assertEquals(token, generatedToken);
    }

    @Test
    void testLoginFailure() {
        String username = "testuser";
        String rawPassword = "password";

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> {
            userService.login(username, rawPassword);
        });
    }
}
