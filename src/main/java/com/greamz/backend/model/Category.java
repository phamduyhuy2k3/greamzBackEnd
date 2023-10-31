package com.greamz.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greamz.backend.enumeration.CategoryTypes;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String image;
    @Enumerated(EnumType.STRING)
    private CategoryTypes categoryTypes;
    @ManyToMany(mappedBy = "categories")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private List<GameModel> gameModels;


}
