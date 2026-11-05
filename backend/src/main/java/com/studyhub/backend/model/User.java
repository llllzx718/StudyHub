package com.studyhub.backend.model;

public record User(
        Long id,
        String username,
        String passwordHash
) {}
