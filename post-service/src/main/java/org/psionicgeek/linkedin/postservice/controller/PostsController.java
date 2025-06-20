package org.psionicgeek.linkedin.postservice.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.psionicgeek.linkedin.postservice.auth.UserContextHolder;
import org.psionicgeek.linkedin.postservice.dto.PostCreateRequestDto;
import org.psionicgeek.linkedin.postservice.dto.PostDto;
import org.psionicgeek.linkedin.postservice.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class PostsController {

    private final PostService postService;


    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto, HttpServletRequest httpServletRequest){


        PostDto createdPost = postService.createPost(postCreateRequestDto);

        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId, HttpServletRequest httpServletRequest) {
       Long userId = UserContextHolder.getCurrentUserId();
        log.info("User ID from request header: {}", userId);
        PostDto postDto = postService.getPostById(postId);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }


    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsByUserId(@PathVariable Long userId) {

        List<PostDto> postDto = postService.getAllPostsOfUser(userId);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }
}
