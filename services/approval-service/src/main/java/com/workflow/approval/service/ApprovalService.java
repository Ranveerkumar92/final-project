package com.workflow.approval.service;

import com.workflow.approval.dto.ApprovalDTO;
import com.workflow.approval.dto.ApprovalStatus;
import com.workflow.approval.entity.Approval;
import com.workflow.approval.repository.ApprovalRepository;
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
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String APPROVAL_TOPIC = "approval-events";

    public ApprovalDTO submitApproval(ApprovalDTO dto, String userId) {
        log.info("Submitting approval for workflow instance: {}", dto.getWorkflowInstanceId());
        
        Approval approval = Approval.builder()
                .workflowInstanceId(dto.getWorkflowInstanceId())
                .approver(userId)
                .status(ApprovalStatus.valueOf(dto.getStatus().name()))
                .comments(dto.getComments())
                .approvalDate(LocalDateTime.now())
                .build();

        Approval saved = approvalRepository.save(approval);
        
        // Publish event to Kafka
        kafkaTemplate.send(APPROVAL_TOPIC, saved.getId().toString(), saved);
        
        return toDTO(saved);
    }

    public ApprovalDTO getApproval(Long id) {
        log.info("Fetching approval: {}", id);
        Approval approval = approvalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Approval not found"));
        return toDTO(approval);
    }

    public List<ApprovalDTO> getPendingApprovalsForUser(String userId) {
        log.info("Fetching pending approvals for user: {}", userId);
        return approvalRepository.findByApprover(userId).stream()
                .filter(a -> "PENDING".equalsIgnoreCase(a.getStatus().name()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ApprovalDTO> getApprovalsForWorkflow(Long workflowInstanceId) {
        log.info("Fetching approvals for workflow instance: {}", workflowInstanceId);
        return approvalRepository.findByWorkflowInstanceId(workflowInstanceId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ApprovalDTO toDTO(Approval approval) {
        return ApprovalDTO.builder()
                .id(approval.getId())
                .workflowInstanceId(approval.getWorkflowInstanceId())
                .approver(approval.getApprover())
                .status(ApprovalStatus.valueOf(approval.getStatus().name()))
                .comments(approval.getComments())
                .build();
    }
}
