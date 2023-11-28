package com.greamz.backend.dto.review;

import com.greamz.backend.dto.account.UserProfileSuperBasic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ReviewOfGame {
    private Long id;
    private String text;
    private int rating;
    private int likes;
    private int dislikes;
    private boolean isTheUserAuthenticatedReacted;
    private boolean reactionType;
    private Date createdAt;
    private UserProfileSuperBasic account;
}
