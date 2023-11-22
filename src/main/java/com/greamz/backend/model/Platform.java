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
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Platform extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;
    private String description;
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<String> devices;
    @OneToMany(mappedBy = "platform", fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<GameModel> gameModels;
    @OneToMany(mappedBy = "platform", fetch = FetchType.LAZY)
    private List<CodeActive> codeActives;
}
