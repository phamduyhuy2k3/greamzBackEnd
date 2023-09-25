package com.greamz.backend.model;

import jakarta.persistence.*;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private int rating;
    private int likes;
    private int dislikes;
    @ManyToOne
    private AccountModel account;
    @ManyToOne
    private GameModel game;
}
