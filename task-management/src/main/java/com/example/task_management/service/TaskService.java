package com.example.task_management.service;

import com.example.task_management.dto.TaskQueryParameters;
import com.example.task_management.entity.Task;
import com.example.task_management.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getTasksByUser(int userId) {
        logger.info("Fetching tasks for userId: {}", userId);
        return taskRepository.findByUserId(userId);
    }

    public Task getById(int id) {
        logger.info("Fetching task by id: {}", id);
        return taskRepository.findById(id).orElse(null);
    }

    public Task getById(int id, int userId) {
        logger.info("Fetching task by id: {} and userId: {}", id, userId);
        return taskRepository.findByIdAndUserId(id, userId).orElse(null);
    }

    public Task add(Task task) {
        logger.info("Adding new task: {}", task.getTitle());
        return taskRepository.save(task);
    }

    public Task update(Task task) {
        logger.info("Updating task with id: {}", task.getId());
        return taskRepository.save(task);
    }

    public void delete(Task task) {
        logger.info("Deleting task with id: {}", task.getId());
        taskRepository.delete(task);
    }

    public List<Task> getFilteredTasks(int userId, TaskQueryParameters query, boolean isAdmin) {
        logger.info("Fetching filtered tasks for userId: {}, isAdmin: {}", userId, isAdmin);

        List<Task> tasks = isAdmin ? taskRepository.findAll() : taskRepository.findByUserId(userId);

        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            boolean isCompleted = query.getStatus().equalsIgnoreCase("completed");
            tasks = tasks.stream()
                    .filter(t -> t.isCompleted() == isCompleted)
                    .toList();
            logger.info("Filtered tasks by status: {}", query.getStatus());
        }

        tasks = tasks.stream()
                .sorted((t1, t2) -> {
                    int compareResult = 0;
                    if ("createdAt".equalsIgnoreCase(query.getSortBy())) {
                        compareResult = t1.getCreatedAt().compareTo(t2.getCreatedAt());
                    } else {
                        compareResult = t1.getDeadline().compareTo(t2.getDeadline());
                    }
                    return query.isDesc() ? -compareResult : compareResult;
                })
                .toList();

        int skip = (query.getPage() - 1) * query.getPageSize();
        List<Task> paginatedTasks = tasks.stream()
                .skip(skip)
                .limit(query.getPageSize())
                .toList();

        logger.info("Returning {} tasks after pagination", paginatedTasks.size());
        return paginatedTasks;
    }

}
