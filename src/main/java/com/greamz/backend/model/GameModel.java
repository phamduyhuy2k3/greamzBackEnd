package com.greamz.backend.model;

import jakarta.persistence.*;

@Entity
public class GameModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String image;
    private String videos;

    @ManyToOne
    private GameCategory gameCategory;


}
