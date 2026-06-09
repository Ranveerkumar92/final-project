package com.workflow.notification.entity;

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
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientId;
    private String subject;
    private String message;
    private String type; // EMAIL, SMS, IN_APP
    private String status; // PENDING, SENT, FAILED
    private LocalDateTime createdDate;
    private LocalDateTime sentDate;
    private String errorDetails;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        status = "PENDING";
    }
}
