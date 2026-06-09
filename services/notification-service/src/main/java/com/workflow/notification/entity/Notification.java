package com.workflow.notification.entity;

import com.workflow.notification.dto.NotificationStatus;
import com.workflow.notification.dto.NotificationType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

    private LocalDateTime sentDate;

    private String errorDetails;

    private String recipientId;

    private String subject;

    private String message;



    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = NotificationStatus.PENDING;
        }
    }
}

