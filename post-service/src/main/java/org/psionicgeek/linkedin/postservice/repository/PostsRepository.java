package org.psionicgeek.linkedin.postservice.repository;

import org.psionicgeek.linkedin.postservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(Long userId);
}

