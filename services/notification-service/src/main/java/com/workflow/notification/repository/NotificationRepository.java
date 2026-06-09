package com.workflow.notification.repository;

import com.workflow.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientId(String recipientId);
    List<Notification> findByStatus(String status);
    List<Notification> findByType(String type);
}
