package com.greamz.backend.dto.review;

import com.greamz.backend.dto.account.UserProfileSuperBasic;
import com.greamz.backend.dto.game.GameBasicDTO;
import com.greamz.backend.enumeration.ReactionType;
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
    private GameBasicDTO game;
    private int rating;
    private int likes;
    private int dislikes;
    private boolean isReacted;
    private ReactionType reactionType;
    private Date createdAt;
    private UserProfileSuperBasic account;
}
