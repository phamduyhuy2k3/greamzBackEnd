package com.greamz.backend.model;


import com.greamz.backend.common.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review extends AbstractAuditEntity {
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
    @ManyToOne
    private Platform platform;
    @OneToMany(mappedBy = "review")
    private List<ReviewReaction> reviewReactions;
}
