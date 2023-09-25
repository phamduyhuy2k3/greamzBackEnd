package com.greamz.backend.model;

import jakarta.persistence.*;

@Entity
public class Disscusion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @ManyToOne
    private AccountModel account;
    @ManyToOne
    private GameModel game;
}
