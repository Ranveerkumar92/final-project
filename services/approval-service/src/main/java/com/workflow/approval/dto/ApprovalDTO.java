package com.workflow.approval.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalDTO {

    private Long id;

    @NotNull(message = "Workflow instance ID is required")
    private Long workflowInstanceId;

    @NotBlank(message = "Approver is required")
    @Size(max = 100, message = "Approver name cannot exceed 100 characters")
    private String approver;

    @NotNull(message = "Approval status is required")
    private ApprovalStatus status;

    @Size(max = 1000, message = "Comments cannot exceed 1000 characters")
    private String comments;
}
