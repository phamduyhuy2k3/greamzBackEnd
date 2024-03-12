package com.greamz.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greamz.backend.common.AbstractAuditEntity;
import com.greamz.backend.enumeration.CategoryTypes;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String image;
    @Transient
    private Long gameCount;
    @Enumerated(EnumType.STRING)
    private CategoryTypes categoryTypes;
    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private List<GameModel> gameModels;


}
