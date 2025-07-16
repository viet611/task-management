package com.example.task_management;

import com.example.task_management.entity.Task;
import com.example.task_management.entity.User;
import com.example.task_management.repository.TaskRepository;
import com.example.task_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("!test")
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User user1 = new User();
            user1.setUsername("user");
            user1.setPasswordHash(passwordEncoder.encode("123456"));
            user1.setRole("USER");
            userRepository.save(user1);

            User user2 = new User();
            user2.setUsername("admin");
            user2.setPasswordHash(passwordEncoder.encode("123456"));
            user2.setRole("ADMIN");
            userRepository.save(user2);

            // Tasks for user1
            Task task1 = new Task();
            task1.setTitle("User Task 1");
            task1.setDescription("Description for User Task 1");
            task1.setCreatedAt(LocalDateTime.now());
            task1.setDeadline(LocalDateTime.now().plusDays(3));
            task1.setUser(user1);
            taskRepository.save(task1);

            Task task2 = new Task();
            task2.setTitle("User Task 2");
            task2.setDescription("Description for User Task 2");
            task2.setCreatedAt(LocalDateTime.now());
            task2.setDeadline(LocalDateTime.now().plusDays(5));
            task2.setUser(user1);
            taskRepository.save(task2);

            Task task3 = new Task();
            task3.setTitle("User Task 3");
            task3.setDescription("Description for User Task 3");
            task3.setCreatedAt(LocalDateTime.now());
            task3.setDeadline(LocalDateTime.now().plusDays(7));
            task3.setUser(user1);
            taskRepository.save(task3);

            Task task4 = new Task();
            task4.setTitle("User Task 4");
            task4.setDescription("Description for User Task 4");
            task4.setCreatedAt(LocalDateTime.now());
            task4.setDeadline(LocalDateTime.now().plusDays(9));
            task4.setUser(user1);
            taskRepository.save(task4);

            // Tasks for admin
            Task task5 = new Task();
            task5.setTitle("Admin Task 1");
            task5.setDescription("Description for Admin Task 1");
            task5.setCreatedAt(LocalDateTime.now());
            task5.setDeadline(LocalDateTime.now().plusDays(4));
            task5.setUser(user2);
            taskRepository.save(task5);

            Task task6 = new Task();
            task6.setTitle("Admin Task 2");
            task6.setDescription("Description for Admin Task 2");
            task6.setCreatedAt(LocalDateTime.now());
            task6.setDeadline(LocalDateTime.now().plusDays(8));
            task6.setUser(user2);
            taskRepository.save(task6);

            Task task7 = new Task();
            task7.setTitle("Admin Task 3");
            task7.setDescription("Description for Admin Task 3");
            task7.setCreatedAt(LocalDateTime.now());
            task7.setDeadline(LocalDateTime.now().plusDays(12));
            task7.setUser(user2);
            taskRepository.save(task7);

            Task task8 = new Task();
            task8.setTitle("Admin Task 4");
            task8.setDescription("Description for Admin Task 4");
            task8.setCreatedAt(LocalDateTime.now());
            task8.setDeadline(LocalDateTime.now().plusDays(15));
            task8.setUser(user2);
            taskRepository.save(task8);

            System.out.println("Seeded initial users and 8 tasks.");
        }
    }
}
