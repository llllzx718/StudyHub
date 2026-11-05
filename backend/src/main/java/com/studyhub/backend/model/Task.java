package com.studyhub.backend.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Task(
        Long id,
        String text,
        boolean done,
        Long userId,
        TaskPriority priority,
        LocalDate deadline,
        LocalDateTime createdAt
) {
}