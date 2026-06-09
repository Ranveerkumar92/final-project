package com.workflow.approval.controller;

import com.workflow.common.dto.ApiResponse;
import com.workflow.approval.dto.ApprovalDTO;
import com.workflow.approval.service.ApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/approvals")
@RequiredArgsConstructor
@Tag(name = "Approval Controller", description = "REST APIs for Approval management")
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping("/submit")
    @Operation(summary = "Submit an approval decision")
    public ResponseEntity<ApiResponse<ApprovalDTO>> submitApproval(
            @RequestBody ApprovalDTO dto,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("Submitting approval");
        ApprovalDTO submitted = approvalService.submitApproval(dto, userId != null ? userId : "system");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(submitted, "Approval submitted successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get approval by ID")
    public ResponseEntity<ApiResponse<ApprovalDTO>> getApproval(@PathVariable Long id) {
        log.info("Fetching approval: {}", id);
        ApprovalDTO approval = approvalService.getApproval(id);
        return ResponseEntity.ok(ApiResponse.success(approval, "Approval retrieved successfully"));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending approvals for current user")
    public ResponseEntity<ApiResponse<List<ApprovalDTO>>> getPendingApprovals(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("Fetching pending approvals for user: {}", userId);
        List<ApprovalDTO> approvals = approvalService.getPendingApprovalsForUser(userId != null ? userId : "system");
        return ResponseEntity.ok(ApiResponse.success(approvals, "Pending approvals retrieved successfully"));
    }

    @GetMapping("/workflow/{workflowInstanceId}")
    @Operation(summary = "Get all approvals for a workflow instance")
    public ResponseEntity<ApiResponse<List<ApprovalDTO>>> getApprovalsByWorkflow(
            @PathVariable Long workflowInstanceId) {
        log.info("Fetching approvals for workflow instance: {}", workflowInstanceId);
        List<ApprovalDTO> approvals = approvalService.getApprovalsForWorkflow(workflowInstanceId);
        return ResponseEntity.ok(ApiResponse.success(approvals, "Approvals retrieved successfully"));
    }
}
