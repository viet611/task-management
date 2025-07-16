package com.example.task_management.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskCreateRequest {

    @NotBlank(message = "Title không được để trống")
    private String title;

    private String description;

    @PastOrPresent(message = "CreatedAt phải là thời gian hiện tại hoặc quá khứ")
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull(message = "Deadline không được để trống")
    @Future(message = "Deadline phải là thời gian trong tương lai")
    private LocalDateTime deadline;

    private boolean completed;
}
