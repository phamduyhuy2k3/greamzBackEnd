package com.greamz.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "cart",uniqueConstraints = @UniqueConstraint(columnNames = {"game_id","account_id","platform_id"}))
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameModel game;
    @ManyToOne
    @JoinColumn(name = "platform_id")
    private Platform platform;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountModel account;

}
