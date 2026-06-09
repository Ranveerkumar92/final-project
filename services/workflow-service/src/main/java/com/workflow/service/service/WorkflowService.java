package com.workflow.service.service;

import com.workflow.service.dto.WorkflowDTO;
import com.workflow.service.entity.Workflow;
import com.workflow.service.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowRepository workflowRepository;

    public WorkflowDTO createWorkflow(WorkflowDTO dto, String userId) {
        log.info("Creating workflow: {}", dto.getName());
        
        Workflow workflow = Workflow.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .definition(dto.getDefinition())
                .createdBy(userId)
                .build();

        Workflow saved = workflowRepository.save(workflow);
        return toDTO(saved);
    }

    public WorkflowDTO getWorkflow(Long id) {
        log.info("Fetching workflow: {}", id);
        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));
        return toDTO(workflow);
    }

    public List<WorkflowDTO> getAllWorkflows() {
        return workflowRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public WorkflowDTO updateWorkflow(Long id, WorkflowDTO dto) {
        log.info("Updating workflow: {}", id);
        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));

        workflow.setName(dto.getName());
        workflow.setDescription(dto.getDescription());
        workflow.setDefinition(dto.getDefinition());

        Workflow updated = workflowRepository.save(workflow);
        return toDTO(updated);
    }

    public void deleteWorkflow(Long id) {
        log.info("Deleting workflow: {}", id);
        workflowRepository.deleteById(id);
    }

    public WorkflowDTO publishWorkflow(Long id) {
        log.info("Publishing workflow: {}", id);
        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));
        workflow.setStatus("PUBLISHED");
        Workflow updated = workflowRepository.save(workflow);
        return toDTO(updated);
    }

    private WorkflowDTO toDTO(Workflow workflow) {
        return WorkflowDTO.builder()
                .id(workflow.getId())
                .name(workflow.getName())
                .description(workflow.getDescription())
                .definition(workflow.getDefinition())
                .status(workflow.getStatus())
                .createdBy(workflow.getCreatedBy())
                .build();
    }
}
