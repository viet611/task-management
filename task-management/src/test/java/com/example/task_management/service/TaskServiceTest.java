package com.example.task_management.service;

import com.example.task_management.dto.TaskQueryParameters;
import com.example.task_management.entity.Task;
import com.example.task_management.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTasksByUser() {
        when(taskRepository.findByUserId(1)).thenReturn(List.of(new Task(), new Task()));

        List<Task> tasks = taskService.getTasksByUser(1);

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findByUserId(1);
    }

    @Test
    void testGetById() {
        Task task = new Task();
        task.setId(1);
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        Task result = taskService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testGetByIdNotFound() {
        when(taskRepository.findById(99)).thenReturn(Optional.empty());

        Task result = taskService.getById(99);

        assertNull(result);
    }

    @Test
    void testAddTask() {
        Task task = new Task();
        task.setTitle("New Task");

        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.add(task);

        assertEquals("New Task", result.getTitle());
        verify(taskRepository).save(task);
    }

    @Test
    void testUpdateTask() {
        Task task = new Task();
        task.setId(1);
        task.setTitle("Updated");

        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.update(task);

        assertEquals("Updated", result.getTitle());
        verify(taskRepository).save(task);
    }

    @Test
    void testDeleteTask() {
        Task task = new Task();
        task.setId(1);

        taskService.delete(task);

        verify(taskRepository).delete(task);
    }

    @Test
    void testGetFilteredTasksForUser() {
        Task task1 = new Task();
        task1.setCompleted(true);
        task1.setCreatedAt(LocalDateTime.now());
        task1.setDeadline(LocalDateTime.now().plusDays(5));

        Task task2 = new Task();
        task2.setCompleted(false);
        task2.setCreatedAt(LocalDateTime.now().minusDays(1));
        task2.setDeadline(LocalDateTime.now().plusDays(3));

        when(taskRepository.findByUserId(1)).thenReturn(Arrays.asList(task1, task2));

        TaskQueryParameters query = new TaskQueryParameters();
        query.setStatus("completed");
        query.setSortBy("deadline");
        query.setDesc(false);
        query.setPage(1);
        query.setPageSize(10);

        List<Task> filteredTasks = taskService.getFilteredTasks(1, query, false);

        assertEquals(1, filteredTasks.size());
        assertTrue(filteredTasks.get(0).isCompleted());
    }

    @Test
    void testGetFilteredTasksForAdmin() {
        Task task1 = new Task();
        task1.setCompleted(false);
        task1.setCreatedAt(LocalDateTime.now());
        task1.setDeadline(LocalDateTime.now().plusDays(5));

        Task task2 = new Task();
        task2.setCompleted(true);
        task2.setCreatedAt(LocalDateTime.now().minusDays(1));
        task2.setDeadline(LocalDateTime.now().plusDays(3));

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        TaskQueryParameters query = new TaskQueryParameters();
        query.setStatus(null);  // không lọc status
        query.setSortBy("createdAt");
        query.setDesc(true);
        query.setPage(1);
        query.setPageSize(10);

        List<Task> filteredTasks = taskService.getFilteredTasks(0, query, true);

        assertEquals(2, filteredTasks.size());
        assertTrue(filteredTasks.get(0).getCreatedAt().isAfter(filteredTasks.get(1).getCreatedAt()));
    }
}
