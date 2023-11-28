package com.greamz.backend.model;


import com.greamz.backend.common.TimeStampEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 600)
    private String text;
    private int rating;
    @ManyToOne
    private AccountModel account;
    @ManyToOne
    private GameModel game;
    @OneToMany(mappedBy = "review")
    private List<ReviewReaction> reviewReactions;
}
