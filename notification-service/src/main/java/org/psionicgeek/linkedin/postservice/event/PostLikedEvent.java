package org.psionicgeek.linkedin.postservice.event;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostLikedEvent {

    Long postId;
    Long creatorId;
    Long likedByUserId;
}
