package com.example.task_management.controller;

import com.example.task_management.dto.ApiResponse;
import com.example.task_management.dto.TaskCreateRequest;
import com.example.task_management.dto.TaskQueryParameters;
import com.example.task_management.entity.Task;
import com.example.task_management.entity.User;
import com.example.task_management.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TaskControllerTest {
    @Mock
    private TaskService taskService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TaskController taskController;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setId(1);
        mockUser.setRole("USER");
        when(authentication.getPrincipal()).thenReturn(mockUser);
    }

    @Test
    void testGetAllTasks() {
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> taskList = Arrays.asList(task1, task2);

        when(taskService.getFilteredTasks(anyInt(), any(TaskQueryParameters.class), anyBoolean())).thenReturn(taskList);

        ApiResponse<List<Task>> response = taskController.getAllTasks(null, "deadline", false, 1, 10, authentication);

        assertTrue(response.isSuccess());
        assertEquals(2, response.getData().size());
        verify(taskService).getFilteredTasks(eq(1), any(TaskQueryParameters.class), eq(false));
    }

    @Test
    void testGetTaskByIdFound() {
        Task mockTask = new Task();
        mockTask.setId(1);

        when(taskService.getById(eq(1), eq(1))).thenReturn(mockTask);

        ApiResponse<Task> response = taskController.getTaskById(1, authentication);

        assertTrue(response.isSuccess());
        assertEquals(1, response.getData().getId());
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskService.getById(eq(99), eq(1))).thenReturn(null);

        assertThrows(RuntimeException.class, () -> taskController.getTaskById(99, authentication));
    }

    @Test
    void testCreateTask() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("New Task");
        request.setDescription("Task description");
        request.setCreatedAt(LocalDateTime.now());
        request.setDeadline(LocalDateTime.now().plusDays(7));
        request.setCompleted(false);

        Task savedTask = new Task();
        savedTask.setId(1);
        savedTask.setTitle("New Task");

        when(taskService.add(any(Task.class))).thenReturn(savedTask);

        ApiResponse<Task> response = taskController.createTask(request, authentication);

        assertTrue(response.isSuccess());
        assertEquals("New Task", response.getData().getTitle());
        verify(taskService).add(any(Task.class));
    }


    @Test
    void testUpdateTask() {
        Task existingTask = new Task();
        existingTask.setId(1);
        existingTask.setUser(mockUser);

        Task updatedTask = new Task();
        updatedTask.setId(1);

        when(taskService.getById(eq(1), eq(1))).thenReturn(existingTask);
        when(taskService.update(any(Task.class))).thenReturn(updatedTask);

        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");

        ApiResponse<Task> response = taskController.updateTask(1, request, authentication);

        assertTrue(response.isSuccess());
        assertEquals(1, response.getData().getId());
        verify(taskService).update(any(Task.class));
    }


    @Test
    void testUpdateTaskNotFound() {
        when(taskService.getById(eq(99), eq(1))).thenReturn(null);

        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Nonexistent Task");

        assertThrows(RuntimeException.class, () -> taskController.updateTask(99, request, authentication));
    }


    @Test
    void testDeleteTask() {
        Task taskToDelete = new Task();
        taskToDelete.setId(1);

        when(taskService.getById(eq(1), eq(1))).thenReturn(taskToDelete);

        ApiResponse<String> response = taskController.deleteTask(1, authentication);

        assertTrue(response.isSuccess());
        verify(taskService).delete(taskToDelete);
    }

    @Test
    void testDeleteTaskNotFound() {
        when(taskService.getById(eq(99), eq(1))).thenReturn(null);

        assertThrows(RuntimeException.class, () -> taskController.deleteTask(99, authentication));
    }
}
