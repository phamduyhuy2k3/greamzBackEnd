package com.greamz.backend.model;

import com.greamz.backend.common.TimeStampEntity;
import com.greamz.backend.enumeration.ReactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table( uniqueConstraints = {
        @UniqueConstraint(columnNames = {"review_id", "user_id"})
})
public class ReviewReaction extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionId;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AccountModel user;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;


}
