package org.psionicgeek.linkedin.postservice.repository;


import org.psionicgeek.linkedin.postservice.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {


    boolean existsByPostIdAndUserId(Long postId, Long userId);

   void deleteByPostIdAndUserId(Long postId, long userId);
}
