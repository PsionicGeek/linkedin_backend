package org.psionicgeek.linkedin.notification_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.psionicgeek.linkedin.notification_service.entity.Notification;
import org.psionicgeek.linkedin.notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendNotification {

    private final NotificationRepository notificationRepository;

    public void send(Long userId, String postContent) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(postContent);
        notificationRepository.save(notification);
        log.info("Notification sent to user {}: {}", userId, postContent);

    }
}
