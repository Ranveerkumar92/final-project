package com.workflow.approval.repository;

import com.workflow.approval.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    List<Approval> findByWorkflowInstanceId(Long workflowInstanceId);
    List<Approval> findByApprover(String approver);
    List<Approval> findByStatus(String status);
}
