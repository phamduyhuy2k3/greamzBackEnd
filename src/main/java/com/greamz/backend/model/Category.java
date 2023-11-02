package com.greamz.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greamz.backend.common.TimeStampEntity;
import com.greamz.backend.enumeration.CategoryTypes;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String image;
    @Enumerated(EnumType.STRING)
    private CategoryTypes categoryTypes;
    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private List<GameModel> gameModels;


}
