package com.example.task_management.dto;

import lombok.Data;

@Data
public class TaskQueryParameters {
    private String status;

    private boolean desc = false;

    private String sortBy = "deadline";

    private int page = 1;

    private int pageSize = 10;
}
