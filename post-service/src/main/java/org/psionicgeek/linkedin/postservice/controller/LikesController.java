package org.psionicgeek.linkedin.postservice.controller;


import lombok.RequiredArgsConstructor;
import org.psionicgeek.linkedin.postservice.services.PostLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {

       postLikeService.likePost(postId, 1L);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> lunLikePost(@PathVariable Long postId) {

        postLikeService.unLikePost(postId, 1L);
        return ResponseEntity.noContent().build();

    }
}
