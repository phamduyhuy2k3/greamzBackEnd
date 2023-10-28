package com.greamz.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.greamz.backend.common.TimeStampEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameModel extends TimeStampEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @JsonProperty("appid")
    private Long appid;
    @JsonProperty("name")
    private String name;
    @Column(length = 100000)
    private String detailed_description;
    @Column(length = 100000)
    private String about_the_game;
    @Column(length = 100000)
    private String short_description;
    @Column(length = 1000)
    private String header_image;
    @Column(length = 1000)
    private String website;
    @Column(length = 1000)
    private String capsule_image;
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<String> images;
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<String> movies;
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "gameModel")
    private List<Screenshot> screenshots;
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JsonIgnore
    private List<GameCategory> gameCategory;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> supported_languages;
}
