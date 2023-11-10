package com.greamz.backend.model;

import com.greamz.backend.common.TimeStampEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Review extends TimeStampEntity {
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
