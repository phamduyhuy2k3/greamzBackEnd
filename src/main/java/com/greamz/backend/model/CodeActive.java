package com.greamz.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.greamz.backend.common.TimeStampEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CodeActive extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Boolean active;
    @ManyToOne(fetch = FetchType.LAZY)
    private AccountModel account;
    @ManyToOne(fetch = FetchType.LAZY)
    private Platform platform;
    @ManyToOne(fetch = FetchType.LAZY)
    private GameModel game;

}
