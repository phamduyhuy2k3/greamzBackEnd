package com.greamz.backend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class GameCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String image;
    @OneToMany(mappedBy = "gameCategory")
    private List<GameModel> games;

}
