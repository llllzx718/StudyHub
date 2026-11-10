package com.studyhub.backend.service;

import com.studyhub.backend.dto.*;
import com.studyhub.backend.model.Task;
import com.studyhub.backend.repository.TaskRepository;
import com.studyhub.backend.exception.TaskNotFoundException;
import com.studyhub.backend.model.TaskPriority;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    //查全部
    public List<Task> getAllTasks(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    //查单个
    public Task getTaskById(Long id,Long userId) {
        return taskRepository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    //新增
    public Task addTask(
            CreateTaskRequest request,
            Long userId
    ) {

        validateTaskText(request.text());
        TaskPriority priority =
                request.priority() == null
                        ? TaskPriority.MEDIUM
                        : request.priority();

        Task task =
                new Task(
                        null,
                        request.text(),
                        false,
                        userId,
                        priority,
                        request.deadline(),
                        null
                );

        return taskRepository.insert(task);
    }

    //修改
    public Task updateTask(Long id, UpdateTaskRequest request,Long userId) {
        Task oldTask = getTaskById(id,userId);

        String newText = oldTask.text();
        boolean newDone = oldTask.done();
        TaskPriority newPriority = oldTask.priority();
        LocalDate newDeadline = oldTask.deadline();

        if (request.text() != null) {
            validateTaskText(request.text());
            newText = request.text();
        }

        if (request.done() != null) {
            newDone = request.done();
        }

        if (request.priority() != null) {
            newPriority = request.priority();
        }

        if (Boolean.TRUE.equals(request.clearDeadline())) {
            newDeadline = null;
        }else if(request.deadline() != null){
            newDeadline = request.deadline();
        }

        Task newTask =
                new Task(
                        id,
                        newText,
                        newDone,
                        userId,
                        newPriority,
                        newDeadline,
                        oldTask.createdAt()
                );

        taskRepository.update(
                id,
                userId,
                newTask
        );

        int affectedRows =
                taskRepository.update(
                        id,
                        userId,
                        newTask
                );

        if (affectedRows == 0) {
            throw new TaskNotFoundException(id);
        }
        return newTask;

    }

    //删除
    public void deleteTask(Long id,Long userId) {
        int affectedRows = taskRepository.delete(id,userId);
        if(affectedRows == 0){
            throw new TaskNotFoundException(id);
        }
    }



    ///PRIVATE///

    private void validateTaskText(String text) {

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("任务内容不能为空");
        }

        if (text.length() > 100) {
            throw new IllegalArgumentException(
                    "任务内容不能超过100个字符"
            );
        }
    }
}
