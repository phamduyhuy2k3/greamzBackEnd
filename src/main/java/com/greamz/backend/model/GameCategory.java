package com.greamz.backend.model;

import com.greamz.backend.enumeration.CategoryTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String image;
    @Enumerated(EnumType.STRING)
    private CategoryTypes categoryTypes;
    @ManyToMany(mappedBy = "gameCategory",cascade = CascadeType.PERSIST)
    private List<GameModel> gameModels;

}
