package com.workflow.task.service;

import com.workflow.task.dto.TaskDTO;
import com.workflow.task.dto.WorkflowStatus;
import com.workflow.task.entity.Task;
import com.workflow.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TASK_TOPIC = "task-events";

    public TaskDTO createTask(TaskDTO dto) {
        log.info("Creating task: {}", dto.getName());
        
        Task task = Task.builder()
                .workflowInstanceId(dto.getWorkflowInstanceId())
                .name(dto.getName())
                .description(dto.getDescription())
                .assignedTo(dto.getAssignedTo())
                .priority(dto.getPriority())
                .build();

        Task saved = taskRepository.save(task);
        kafkaTemplate.send(TASK_TOPIC, saved.getId().toString(), saved);
        
        return toDTO(saved);
    }

    public TaskDTO getTask(Long id) {
        log.info("Fetching task: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return toDTO(task);
    }

    public List<TaskDTO> getTasksByUser(String userId) {
        log.info("Fetching tasks for user: {}", userId);
        return taskRepository.findByAssignedTo(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO updateTaskStatus(Long id, String status) {
        log.info("Updating task status: {} to {}", id, status);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        task.setStatus(WorkflowStatus.valueOf(status));
        if ("COMPLETED".equals(status)) {
            task.setCompletedDate(LocalDateTime.now());
        }
        
        Task updated = taskRepository.save(task);
        kafkaTemplate.send(TASK_TOPIC, updated.getId().toString(), updated);
        
        return toDTO(updated);
    }

    private TaskDTO toDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .workflowInstanceId(task.getWorkflowInstanceId())
                .name(task.getName())
                .description(task.getDescription())
                .assignedTo(task.getAssignedTo())
                .status(task.getStatus())
                .priority(task.getPriority())
                .build();
    }
}
