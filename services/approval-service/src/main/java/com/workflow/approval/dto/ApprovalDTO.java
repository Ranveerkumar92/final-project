package com.workflow.approval.dto;

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
    private Long workflowInstanceId;
    private String approver;
    private String status;
    private String comments;
}
