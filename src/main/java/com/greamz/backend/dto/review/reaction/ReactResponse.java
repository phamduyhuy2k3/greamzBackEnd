package com.greamz.backend.dto.review.reaction;

import com.greamz.backend.enumeration.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReactResponse {
    private int likes;
    private int dislikes;
    private boolean isReacted;
    private ReactionType reactionType;

}
