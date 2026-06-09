package com.workflow.notification.controller;

import com.workflow.common.dto.ApiResponse;
import com.workflow.notification.dto.NotificationDTO;
import com.workflow.notification.service.NotificationService;
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
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Controller", description = "REST APIs for Notification management")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    @Operation(summary = "Send a notification")
    public ResponseEntity<ApiResponse<NotificationDTO>> sendNotification(@RequestBody NotificationDTO dto) {
        log.info("Sending notification to: {}", dto.getRecipientId());
        NotificationDTO sent = notificationService.sendNotification(dto.getRecipientId(), dto.getSubject(), dto.getMessage(), dto.getType());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(sent, "Notification sent successfully"));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get notifications for a user")
    public ResponseEntity<ApiResponse<List<NotificationDTO>>> getNotificationsByUser(@PathVariable String userId) {
        log.info("Fetching notifications for user: {}", userId);
        List<NotificationDTO> notifications = notificationService.getNotificationsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(notifications, "Notifications retrieved successfully"));
    }
}
