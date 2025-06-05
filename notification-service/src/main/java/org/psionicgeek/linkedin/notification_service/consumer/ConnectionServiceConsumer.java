package org.psionicgeek.linkedin.notification_service.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.psionicgeek.linkedin.connection_service.event.AcceptConnectionRequestEvent;
import org.psionicgeek.linkedin.connection_service.event.SendConnectionRequestEvent;
import org.psionicgeek.linkedin.notification_service.repository.NotificationRepository;
import org.psionicgeek.linkedin.notification_service.service.SendNotification;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionServiceConsumer {

    private final SendNotification sendNotification;

    @KafkaListener(topics = "send-connection-request")
    public void consumeSendConnectionRequestEvent(SendConnectionRequestEvent message) {
        log.info("Received send connection request event: {}", message);

        sendNotification.send(message.getReceiverId(), message.getMessage());
    }


    @KafkaListener(topics = "accept-connection-request")
    public void consumeAcceptConnectionRequestEvent(AcceptConnectionRequestEvent message) {
        log.info("Received accept connection request event: {}", message);

        sendNotification.send(message.getSenderId(), message.getMessage());
    }

}
