package com.studyhub.backend.dto;

import com.studyhub.backend.model.TaskPriority;

import java.time.LocalDate;

public record UpdateTaskRequest(
        String text,
        Boolean done,
        TaskPriority priority,
        LocalDate deadline,
        Boolean clearDeadline
) {
}
