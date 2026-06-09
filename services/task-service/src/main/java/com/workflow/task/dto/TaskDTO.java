package com.workflow.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private Long workflowInstanceId;
    private String name;
    private String description;
    private String assignedTo;
    private String status;
    private Integer priority;
}
