package com.greamz.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameCategoryDTO {
    private Long appid;
    private String name;
    private String detailed_description;
    private String about_the_game;
    private String short_description;
    private String header_image;
    private String website;
    private String capsule_image;
    private Integer stock;
    private List<CategoryBasicDTO> categories;

}
