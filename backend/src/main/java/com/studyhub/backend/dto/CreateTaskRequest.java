package com.studyhub.backend.dto;

import com.studyhub.backend.model.TaskPriority;

import java.time.LocalDate;

public record CreateTaskRequest(
        String text,
        TaskPriority priority,
        LocalDate deadline
) {
}