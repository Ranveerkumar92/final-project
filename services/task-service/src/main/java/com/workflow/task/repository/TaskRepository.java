package com.workflow.task.repository;

import com.workflow.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByWorkflowInstanceId(Long workflowInstanceId);
    List<Task> findByAssignedTo(String assignedTo);
    List<Task> findByStatus(String status);
}
