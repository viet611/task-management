package com.example.task_management.controller;

import com.example.task_management.dto.ApiResponse;
import com.example.task_management.dto.TaskCreateRequest;
import com.example.task_management.dto.TaskQueryParameters;
import com.example.task_management.entity.Task;
import com.example.task_management.entity.User;
import com.example.task_management.service.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ApiResponse<List<Task>> getAllTasks(@RequestParam(required = false) String status,
                                               @RequestParam(defaultValue = "deadline") String sortBy,
                                               @RequestParam(defaultValue = "false") boolean desc,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int pageSize,
                                               Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();
        logger.info("Fetching tasks for userId: {}, status: {}, sortBy: {}, desc: {}, page: {}, pageSize: {}",
                currentUser.getId(), status, sortBy, desc, page, pageSize);

        TaskQueryParameters params = new TaskQueryParameters();
        params.setStatus(status);
        params.setSortBy(sortBy);
        params.setDesc(desc);
        params.setPage(page);
        params.setPageSize(pageSize);

        boolean isAdmin = currentUser.getRole().equalsIgnoreCase("ADMIN");
        List<Task> tasks = taskService.getFilteredTasks(currentUser.getId(), params, isAdmin);

        logger.info("Fetched {} tasks for userId: {}", tasks.size(), currentUser.getId());
        return new ApiResponse<>(true, "Fetched tasks successfully", tasks);
    }

    @GetMapping("/{id}")
    public ApiResponse<Task> getTaskById(@PathVariable int id, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        logger.info("Fetching task by id: {} for userId: {}", id, currentUser.getId());

        Task task = taskService.getById(id, currentUser.getId());
        if (task == null) {
            logger.warn("Task with id: {} not found or unauthorized for userId: {}", id, currentUser.getId());
            throw new RuntimeException("Task not found or unauthorized");
        }
        logger.info("Task with id: {} fetched successfully for userId: {}", id, currentUser.getId());
        return new ApiResponse<>(true, "Task fetched successfully", task);
    }

    @PostMapping
    public ApiResponse<Task> createTask(@RequestBody @Valid TaskCreateRequest request, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        logger.info("Creating new task for userId: {} with title: {}", currentUser.getId(), request.getTitle());

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCreatedAt(request.getCreatedAt());
        task.setDeadline(request.getDeadline());
        task.setCompleted(request.isCompleted());
        task.setUser(currentUser);

        Task createdTask = taskService.add(task);
        logger.info("Task created with id: {} for userId: {}", createdTask.getId(), currentUser.getId());
        return new ApiResponse<>(true, "Task created successfully", createdTask);
    }

    @PutMapping("/{id}")
    public ApiResponse<Task> updateTask(@PathVariable int id, @RequestBody @Valid TaskCreateRequest request, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        logger.info("Updating task with id: {} for userId: {}", id, currentUser.getId());

        Task existingTask = taskService.getById(id, currentUser.getId());
        if (existingTask != null) {
            existingTask.setTitle(request.getTitle());
            existingTask.setDescription(request.getDescription());
            existingTask.setCreatedAt(request.getCreatedAt());
            existingTask.setDeadline(request.getDeadline());
            existingTask.setCompleted(request.isCompleted());

            Task updatedTask = taskService.update(existingTask);
            logger.info("Task with id: {} updated successfully for userId: {}", id, currentUser.getId());
            return new ApiResponse<>(true, "Task updated successfully", updatedTask);
        }
        logger.warn("Update failed. Task with id: {} not found or unauthorized for userId: {}", id, currentUser.getId());
        throw new RuntimeException("Task not found or unauthorized");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteTask(@PathVariable int id, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        logger.info("Deleting task with id: {} for userId: {}", id, currentUser.getId());

        Task task = taskService.getById(id, currentUser.getId());
        if (task != null) {
            taskService.delete(task);
            logger.info("Task with id: {} deleted successfully for userId: {}", id, currentUser.getId());
            return new ApiResponse<>(true, "Task deleted successfully", null);
        } else {
            logger.warn("Delete failed. Task with id: {} not found or unauthorized for userId: {}", id, currentUser.getId());
            throw new RuntimeException("Task not found or unauthorized");
        }
    }
}
