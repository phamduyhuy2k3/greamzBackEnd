package com.greamz.backend.dto.review.reaction;

import com.greamz.backend.enumeration.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReactTheReview {
    private Long reviewId;
    private boolean isReacted;
    private ReactionType reactionType;
    private Integer userId;

}
