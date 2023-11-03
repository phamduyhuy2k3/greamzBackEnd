package com.greamz.backend.model;

import com.greamz.backend.common.TimeStampEntity;
import jakarta.persistence.*;

@Entity
public class Comment extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private int likes;
    private int dislikes;
    @ManyToOne
    private GameModel gameModel;
    @ManyToOne
    private AccountModel account;


}
