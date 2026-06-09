package com.workflow.notification.service;

import com.workflow.notification.dto.NotificationDTO;
import com.workflow.notification.entity.Notification;
import com.workflow.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @KafkaListener(topics = {"approval-events", "task-events", "workflow-events"}, groupId = "notification-service-group")
    public void handleWorkflowEvents(String eventData) {
        log.info("Received workflow event: {}", eventData);
        // Process event and send notification
        sendNotification("system@workflow.com", "Workflow Event", eventData, "IN_APP");
    }

    public NotificationDTO sendNotification(String recipientId, String subject, String message, String type) {
        log.info("Sending notification to: {}", recipientId);
        
        Notification notification = Notification.builder()
                .recipientId(recipientId)
                .subject(subject)
                .message(message)
                .type(type)
                .build();

        try {
            if ("EMAIL".equals(type)) {
                sendEmail(recipientId, subject, message);
            }
            notification.setStatus("SENT");
            notification.setSentDate(LocalDateTime.now());
        } catch (Exception e) {
            log.error("Failed to send notification", e);
            notification.setStatus("FAILED");
            notification.setErrorDetails(e.getMessage());
        }

        Notification saved = notificationRepository.save(notification);
        return toDTO(saved);
    }

    public List<NotificationDTO> getNotificationsByUser(String userId) {
        log.info("Fetching notifications for user: {}", userId);
        return notificationRepository.findByRecipientId(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private void sendEmail(String recipient, String subject, String message) {
        log.info("Sending email to: {}", recipient);
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(recipient);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailMessage.setFrom("noreply@workflow-builder.com");
            mailSender.send(mailMessage);
        } catch (Exception e) {
            log.error("Error sending email", e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private NotificationDTO toDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .recipientId(notification.getRecipientId())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .type(notification.getType())
                .status(notification.getStatus())
                .build();
    }
}
