package com.greamz.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

public class GameDTO {
    private Long appid;
    private String name;
    private String detailed_description;

    private String about_the_game;

    private String short_description;

    private String header_image;

    private String website;

    private String capsule_image;
    private Integer stock;
}
