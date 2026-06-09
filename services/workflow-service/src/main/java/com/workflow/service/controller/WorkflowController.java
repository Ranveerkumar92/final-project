package com.workflow.service.controller;

import com.workflow.common.dto.ApiResponse;
import com.workflow.service.dto.WorkflowDTO;
import com.workflow.service.service.WorkflowService;
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
@RequestMapping("/workflows")
@RequiredArgsConstructor
@Tag(name = "Workflow Controller", description = "REST APIs for Workflow management")
public class WorkflowController {

    private final WorkflowService workflowService;

    @PostMapping
    @Operation(summary = "Create a new workflow")
    public ResponseEntity<ApiResponse<WorkflowDTO>> createWorkflow(
            @RequestBody WorkflowDTO dto,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        log.info("Creating workflow: {}", dto.getName());
        WorkflowDTO created = workflowService.createWorkflow(dto, userId != null ? userId : "system");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Workflow created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get workflow by ID")
    public ResponseEntity<ApiResponse<WorkflowDTO>> getWorkflow(@PathVariable Long id) {
        log.info("Fetching workflow: {}", id);
        WorkflowDTO workflow = workflowService.getWorkflow(id);
        return ResponseEntity.ok(ApiResponse.success(workflow, "Workflow retrieved successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all workflows")
    public ResponseEntity<ApiResponse<List<WorkflowDTO>>> getAllWorkflows() {
        log.info("Fetching all workflows");
        List<WorkflowDTO> workflows = workflowService.getAllWorkflows();
        return ResponseEntity.ok(ApiResponse.success(workflows, "Workflows retrieved successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a workflow")
    public ResponseEntity<ApiResponse<WorkflowDTO>> updateWorkflow(
            @PathVariable Long id,
            @RequestBody WorkflowDTO dto) {
        log.info("Updating workflow: {}", id);
        WorkflowDTO updated = workflowService.updateWorkflow(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Workflow updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a workflow")
    public ResponseEntity<ApiResponse<Void>> deleteWorkflow(@PathVariable Long id) {
        log.info("Deleting workflow: {}", id);
        workflowService.deleteWorkflow(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Workflow deleted successfully"));
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish a workflow")
    public ResponseEntity<ApiResponse<WorkflowDTO>> publishWorkflow(@PathVariable Long id) {
        log.info("Publishing workflow: {}", id);
        WorkflowDTO published = workflowService.publishWorkflow(id);
        return ResponseEntity.ok(ApiResponse.success(published, "Workflow published successfully"));
    }
}
