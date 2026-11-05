package com.studyhub.backend.dto;

public record LoginRequest(
        String username,
        String password
) {
}
