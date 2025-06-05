package org.psionicgeek.linkedin.postservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.psionicgeek.linkedin.postservice.auth.UserContextHolder;
import org.psionicgeek.linkedin.postservice.entity.Post;
import org.psionicgeek.linkedin.postservice.entity.PostLike;
import org.psionicgeek.linkedin.postservice.event.PostLikedEvent;
import org.psionicgeek.linkedin.postservice.exception.BadRequestException;
import org.psionicgeek.linkedin.postservice.exception.ResourceNotFoundException;
import org.psionicgeek.linkedin.postservice.repository.PostLikeRepository;
import org.psionicgeek.linkedin.postservice.repository.PostsRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostsRepository postsRepository;

    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    public void likePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Liking post with id {}.", postId);

        Post post = postsRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));


        boolean alreadyLiked = postLikeRepository.existsByPostIdAndUserId(postId, userId);
        if (alreadyLiked) {
            log.error("User with ID {} has already liked post with ID {}", userId, postId);
            throw new BadRequestException("User has already liked this post");
        }
        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);

        postLikeRepository.save(postLike);
        log.info("Post with ID {} liked by user with ID {}", postId, userId);
        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .postId(postId)
                .creatorId(
                        post.getUserId()
                ).likedByUserId(userId)
                .build();
        kafkaTemplate.send("post-liked-topic",postId, postLikedEvent);

    }

    @Transactional
    public void unLikePost(Long postId, long userId) {
        boolean exists = postsRepository.existsById(postId);

        if (!exists) {
            log.error("Post with ID {} does not exist", postId);
            throw new ResourceNotFoundException("Post not found with ID: " + postId);
        }

        boolean alreadyLiked = postLikeRepository.existsByPostIdAndUserId(postId, userId);
        if (!alreadyLiked) {
            log.error("User with ID {} has not liked post with ID {}", userId, postId);
            throw new BadRequestException("User has not liked this post");
        }
        postLikeRepository.deleteByPostIdAndUserId(postId, userId);


    }
}
