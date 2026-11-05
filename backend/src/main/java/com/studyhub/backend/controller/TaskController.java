package com.studyhub.backend.controller;


import com.studyhub.backend.model.Task;
import com.studyhub.backend.service.TaskService;
import com.studyhub.backend.dto.*;

import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;

import java.util.List;


@CrossOrigin(
        origins = "http://127.0.0.1:5500",
        allowCredentials = "true"
)
@RestController
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //查全部
    @GetMapping("/api/tasks")
    public List<Task> getTasks(
            HttpSession session
    ) {
        Long userId = getCurrentUserId(session);

        return taskService.getAllTasks(userId);
    }

    //查单个
    @GetMapping("/api/tasks/{id}")
    public Task getTaskById(
            @PathVariable Long id,
            HttpSession session
    ) {
        Long userId = getCurrentUserId(session);

        return taskService.getTaskById(
                id,
                userId
        );
    }

    //创建
    @PostMapping("/api/tasks")
    public ResponseEntity<Task> addTask(
            @RequestBody CreateTaskRequest request,
            HttpSession session
    ) {
        Long userId = getCurrentUserId(session);

        Task savedTask =
                taskService.addTask(
                        request,
                        userId
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedTask);
    }

    //修改
    @PatchMapping("/api/tasks/{id}")
    public Task updateTask(
            @PathVariable Long id,
            @RequestBody UpdateTaskRequest request,
            HttpSession session
    ) {
        Long userId = getCurrentUserId(session);

        return taskService.updateTask(
                id,
                request,
                userId
        );
    }

    //删除
    @DeleteMapping("/api/tasks/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            HttpSession session
    ) {
        Long userId = getCurrentUserId(session);

        taskService.deleteTask(
                id,
                userId
        );

        return ResponseEntity.noContent().build();
    }

    private Long getCurrentUserId(HttpSession session) {

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "请先登录"
            );
        }

        return userId;
    }


}

