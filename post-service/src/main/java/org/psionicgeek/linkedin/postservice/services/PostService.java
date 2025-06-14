package org.psionicgeek.linkedin.postservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.psionicgeek.linkedin.postservice.auth.UserContextHolder;
import org.psionicgeek.linkedin.postservice.clients.ConnectionsClient;
import org.psionicgeek.linkedin.postservice.dto.PersonDto;
import org.psionicgeek.linkedin.postservice.dto.PostCreateRequestDto;
import org.psionicgeek.linkedin.postservice.dto.PostDto;
import org.psionicgeek.linkedin.postservice.entity.Post;
import org.psionicgeek.linkedin.postservice.event.PostCreatedEvent;
import org.psionicgeek.linkedin.postservice.exception.ResourceNotFoundException;
import org.psionicgeek.linkedin.postservice.repository.PostsRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostsRepository postsRepository;
    private final ModelMapper  modelMapper;
    private final ConnectionsClient connectionsClient;
    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;


    public PostDto createPost(PostCreateRequestDto postCreateRequestDto) {
        Long userId = UserContextHolder.getCurrentUserId();
        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);
        Post savedPost = postsRepository.save(post);
        log.info("Post created with ID: {}", savedPost.getId());

        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .creatorId(userId)
                .content(savedPost.getContent())
                .postId(savedPost.getId())
                .build();
        kafkaTemplate.send("post-created-topic", postCreatedEvent);
        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {

        Long userId = UserContextHolder.getCurrentUserId();
        List<PersonDto> firstConnections = connectionsClient.getFirstDegreeConnections();
        if (firstConnections.isEmpty()) {
            log.warn("No first degree connections found for user with ID: {}", userId);
            // throw new ResourceNotFoundException("No first degree connections found for user with ID: " + userId);
        } else{

            log.info("First degree connections found for user with ID: {}", userId);
            log.info("First degree connections: {}", firstConnections);
    }
        Post post = postsRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));
        log.info("Post retrieved with ID: {}", post.getId());
        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        List<Post> posts = postsRepository.findAllByUserId(userId);
        if (posts.isEmpty()) {
            log.warn("No posts found for user with ID: {}", userId);
            throw new ResourceNotFoundException("No posts found for user with ID: " + userId);
        }
        log.info("Posts retrieved for user with ID: {}", userId);
        return posts.stream().map(post -> modelMapper.map(post, PostDto.class)).toList();
    }
}
