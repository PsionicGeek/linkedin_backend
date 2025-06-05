package org.psionicgeek.linkedin.connection_service.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.psionicgeek.linkedin.connection_service.auth.UserContextHolder;
import org.psionicgeek.linkedin.connection_service.entity.Person;
import org.psionicgeek.linkedin.connection_service.event.AcceptConnectionRequestEvent;
import org.psionicgeek.linkedin.connection_service.event.SendConnectionRequestEvent;
import org.psionicgeek.linkedin.connection_service.repository.PersonRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConnectionService {

    private final PersonRepository connectionRepository;
    private final KafkaTemplate<String, SendConnectionRequestEvent> sendConnectionRequestEventKafkaTemplate;
    private final KafkaTemplate<String, AcceptConnectionRequestEvent> acceptConnectionRequestEventKafkaTemplate;

    public List<Person> getFirstDegreeConnections() {

        Long userId = UserContextHolder.getCurrentUserId();
        return connectionRepository.getFirstDegreeConnections(userId);
    }

    public Boolean sendConnectionRequest(Long receiverId) {
        Long senderId = UserContextHolder.getCurrentUserId();

        log.info("Sending {} connection requests to {}", receiverId, senderId);

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot send a connection request to yourself.");
        }

        if (connectionRepository.connectionRequestExists(senderId, receiverId)) {
            throw new IllegalStateException("Connection request already exists, cannot send another request.");
        }
        if (connectionRepository.alreadyConnected(senderId, receiverId)) {
           throw  new IllegalStateException("Connection request already exists, cannot send another request.");
        }

        connectionRepository.addConnectionRequest(senderId, receiverId);
        log.info("Connection request sent from user {} to user {}", senderId, receiverId);
        SendConnectionRequestEvent event = SendConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .message("You have a new connection request from user " + senderId)
                .build();
        sendConnectionRequestEventKafkaTemplate.send("send-connection-request", event);
        return  true;


    }

    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        log.info("Accepting connection request from {} to {}", senderId, receiverId);

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot accept a connection request from yourself.");
        }

        if (!connectionRepository.connectionRequestExists(senderId, receiverId)) {
            throw new IllegalStateException("No connection request exists from user " + senderId + " to user " + receiverId);
        }

       connectionRepository.addConnection(senderId, receiverId);
        log.info("Connection request accepted from user {} to user {}", senderId, receiverId);
        AcceptConnectionRequestEvent event = AcceptConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .message("Your connection request has been accepted by user " + receiverId)
                .build();
        acceptConnectionRequestEventKafkaTemplate.send("accept-connection-request", event);
        return true;

    }

    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        log.info("Rejecting connection request from {} to {}", senderId, receiverId);

        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot reject a connection request from yourself.");
        }

        if (!connectionRepository.connectionRequestExists(senderId, receiverId)) {
            throw new IllegalStateException("No connection request exists from user " + senderId + " to user " + receiverId);
        }

        connectionRepository.deleteConnectionRequest(senderId, receiverId);
        log.info("Connection request rejected from user {} to user {}", senderId, receiverId);
        return true;

    }
}
