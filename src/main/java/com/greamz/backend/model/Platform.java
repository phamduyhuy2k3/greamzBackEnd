package com.greamz.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.greamz.backend.common.TimeStampEntity;
import com.greamz.backend.enumeration.Devices;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Platform extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private Devices devices;
    @OneToMany(mappedBy = "platform", fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<GameModel> gameModels;
}
