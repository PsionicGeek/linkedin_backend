package org.psionicgeek.linkedin.notification_service.consumer;


import org.psionicgeek.linkedin.notification_service.auth.UserContextHolder;
import org.psionicgeek.linkedin.notification_service.clients.ConnectionsClient;
import org.psionicgeek.linkedin.notification_service.dto.PersonDto;
import org.psionicgeek.linkedin.notification_service.service.SendNotification;
import org.psionicgeek.linkedin.postservice.event.PostCreatedEvent;
import org.psionicgeek.linkedin.postservice.event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceConsumer {
    private final ConnectionsClient connectionsClient;
    private final SendNotification sendNotification;

    @KafkaListener(topics = "post-created-topic")
    public void consumePostCreatedEvent(PostCreatedEvent postCreatedEvent) {
        UserContextHolder.setCurrentUserId(postCreatedEvent.getCreatorId());
        List<PersonDto> firstConnections = connectionsClient.getFirstDegreeConnections();

        for (PersonDto connection : firstConnections) {
            log.info("Notifying connection {} about new post created by user {}", connection.getName(), postCreatedEvent.getCreatorId());
            // Here you can implement the logic to notify the connection about the new post
            sendNotification.send(connection.getUserId(), "Your friend " + postCreatedEvent.getCreatorId() + " has created a new post! Check it out now!!");
        }


        // Process the post created event
    }

    @KafkaListener(topics = "post-liked-topic")
    public void consumePostLikedEvent(PostLikedEvent postLikedEvent) {


        log.info("Notifying creator {} about post liked by user {}", postLikedEvent.getCreatorId(), postLikedEvent.getCreatorId());
        // Here you can implement the logic to notify the connection about the post like
        sendNotification.send(postLikedEvent.getCreatorId(), "Your friend " + postLikedEvent.getLikedByUserId() + " has liked a post! " + postLikedEvent.getPostId() + " Check it out now!!");

    }


}
