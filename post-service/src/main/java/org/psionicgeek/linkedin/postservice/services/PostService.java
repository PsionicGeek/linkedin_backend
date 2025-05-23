package org.psionicgeek.linkedin.postservice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.psionicgeek.linkedin.postservice.dto.PostCreateRequestDto;
import org.psionicgeek.linkedin.postservice.dto.PostDto;
import org.psionicgeek.linkedin.postservice.entity.Post;
import org.psionicgeek.linkedin.postservice.exception.ResourceNotFoundException;
import org.psionicgeek.linkedin.postservice.repository.PostsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostsRepository postsRepository;
    private final ModelMapper  modelMapper;


    public PostDto createPost(PostCreateRequestDto postCreateRequestDto, Long userId) {

        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);
        Post savedPost = postsRepository.save(post);
        log.info("Post created with ID: {}", savedPost.getId());
        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
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
