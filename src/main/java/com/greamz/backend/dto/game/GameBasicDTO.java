package com.greamz.backend.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameBasicDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long appid;
    private String name;
    private String detailed_description;
    private String about_the_game;
    private String short_description;
    private String header_image;
    private String website;
    private String capsule_image;
    private Integer discount;
    private Double price;

}
