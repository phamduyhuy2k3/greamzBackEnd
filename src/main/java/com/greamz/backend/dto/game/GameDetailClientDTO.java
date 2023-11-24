package com.greamz.backend.dto.game;

import com.greamz.backend.dto.platform.PlatformDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameDetailClientDTO {
    private Long appid;
    private String name;
    private Double price;
    private String detailed_description;
    private String about_the_game;
    private String short_description;
    private String header_image;
    private String website;
    private String capsule_image;
    private Integer discount;
    private Set<String> images;
    private Set<String> movies;
    private List<PlatformDTO> platforms;
    private List<GenreDTO> categories;
}
