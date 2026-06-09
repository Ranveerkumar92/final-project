package com.workflow.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowDTO {
    private Long id;
    private String name;
    private String description;
    private String definition;
    private String status;
    private String createdBy;
}
