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
@Builder
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

    private Double price;
    private Integer discount;
    @ElementCollection()
    private Set<String> images;
    @ElementCollection()

    private Set<String> movies;
    @ManyToMany(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(name = "game_category",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> supported_languages;
    @ManyToMany(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(name = "game_platform",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "platform_id")
    )
    private List<Platform> platforms;
    @OneToMany(mappedBy = "game")
    private List<Review> reviews;
    @OneToMany(mappedBy = "game")
    private List<CodeActive> codeActives;

}
