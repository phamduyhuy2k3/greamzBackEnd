package com.greamz.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameModel {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    @Id
    @JsonProperty("appid")
    private Long appid;
    @JsonProperty("name")
    private String name;
    private String type;
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
    @ElementCollection
    private List<String> images;
    @OneToMany(cascade = {CascadeType.ALL},mappedBy = "gameModel")
    private List<Movie> movies;
    @OneToMany(cascade = {CascadeType.ALL},mappedBy = "gameModel")
    private List<Screenshot> screenshots;

}
