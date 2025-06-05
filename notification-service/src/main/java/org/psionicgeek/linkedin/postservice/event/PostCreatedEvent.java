package org.psionicgeek.linkedin.postservice.event;


import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreatedEvent {

    Long creatorId;
    String content;
    Long postId;

}
