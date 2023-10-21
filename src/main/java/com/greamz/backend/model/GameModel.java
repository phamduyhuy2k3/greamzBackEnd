package com.greamz.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class GameModel extends TimeStampEntity{

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
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
    @Column(length = 100000)
    private String supported_languages;
    private String header_image;
    private String website;
    private String capsule_image;
    @OneToMany(mappedBy = "gameModel", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)

    private Set<Image> images;
    @OneToMany(cascade = {CascadeType.ALL},mappedBy = "gameModel",fetch = FetchType.LAZY)
    private List<Movie> movies;
    @OneToMany(cascade = {CascadeType.ALL},mappedBy = "gameModel")
    private List<Screenshot> screenshots;
    @ManyToMany(cascade = {CascadeType.PERSIST})
    private List<GameCategory> gameCategory;

}
