package com.workflow.task.controller;

import com.workflow.common.dto.ApiResponse;
import com.workflow.task.dto.TaskDTO;
import com.workflow.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Controller", description = "REST APIs for Task management")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<ApiResponse<TaskDTO>> createTask(@Valid @RequestBody TaskDTO dto) {
        log.info("Creating task: {}", dto.getName());
        TaskDTO created = taskService.createTask(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Task created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<ApiResponse<TaskDTO>> getTask(@PathVariable Long id) {
        log.info("Fetching task: {}", id);
        TaskDTO task = taskService.getTask(id);
        return ResponseEntity.ok(ApiResponse.success(task, "Task retrieved successfully"));
    }

    @GetMapping("/assigned/{userId}")
    @Operation(summary = "Get tasks assigned to a user")
    public ResponseEntity<ApiResponse<List<TaskDTO>>> getTasksByUser(@PathVariable String userId) {
        log.info("Fetching tasks for user: {}", userId);
        List<TaskDTO> tasks = taskService.getTasksByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(tasks, "Tasks retrieved successfully"));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update task status")
    public ResponseEntity<ApiResponse<TaskDTO>> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        log.info("Updating task status: {} to {}", id, status);
        TaskDTO updated = taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(updated, "Task status updated successfully"));
    }
}
