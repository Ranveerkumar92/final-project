package com.workflow.service.repository;

import com.workflow.service.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Long> {
    List<Workflow> findByStatus(String status);
    List<Workflow> findByCreatedBy(String createdBy);
}
