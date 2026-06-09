package com.workflow.task.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long workflowInstanceId;
    private String name;
    private String description;
    private String assignedTo;
    private String status; // PENDING, IN_PROGRESS, COMPLETED, FAILED
    private Integer priority;
    private LocalDateTime dueDate;
    private LocalDateTime createdDate;
    private LocalDateTime completedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        status = "PENDING";
    }
}
