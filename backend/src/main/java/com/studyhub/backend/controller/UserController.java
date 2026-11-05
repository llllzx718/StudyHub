package com.studyhub.backend.controller;

import com.studyhub.backend.dto.LoginRequest;
import com.studyhub.backend.dto.RegisterRequest;
import com.studyhub.backend.dto.UserResponse;
import com.studyhub.backend.model.User;
import com.studyhub.backend.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(
        origins = "http://127.0.0.1:5500",
        allowCredentials = "true"
)
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/api/users/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody RegisterRequest request
    ) {

        UserResponse user =
                userService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @PostMapping("/api/users/login")
    public UserResponse login(
            @RequestBody LoginRequest request,
            HttpSession session
    ) {

        UserResponse user =
                userService.login(request);

        session.setAttribute(
                "userId",
                user.id()
        );

        return user;
    }

    @GetMapping("/api/users/me")
    public ResponseEntity<UserResponse> me(
            HttpSession session
    ) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        UserResponse user =
                userService.getUserById(userId);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/users/logout")
    public ResponseEntity<Void> logout(
            HttpSession session
    ) {

        session.invalidate();

        return ResponseEntity
                .noContent()
                .build();
    }
}