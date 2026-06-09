package com.workflow.approval.entity;

import com.workflow.approval.dto.ApprovalStatus;
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
@Table(name = "approvals")
public class Approval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long workflowInstanceId;
    private String approver;
    private ApprovalStatus status;
    private String comments;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime approvalDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
        status = ApprovalStatus.PENDING; // Default status
    }
}
