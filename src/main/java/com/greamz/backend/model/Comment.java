package com.greamz.backend.model;

import jakarta.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private int likes;
    private int dislikes;
    @ManyToOne
    private Disscusion disscusion;
    @ManyToOne
    private AccountModel account;
    @ManyToOne
    private ArtWork artWork;

}
