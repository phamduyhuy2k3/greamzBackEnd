package com.greamz.backend.dto.game;

import com.greamz.backend.dto.platform.PlatformDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
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
    private String header_image;
    private String website;
    private String capsule_image;
    private Integer discount;
    private Integer totalReviewed;
    private Short averageRating;
    private Set<String> images;
    private Set<String> movies;
    private List<PlatformDTO> platforms;
//    private List<GenreDTO> categories;

    public GameDetailClientDTO(Long appid, String name, Double price, String header_image) {
        this.appid = appid;
        this.name = name;
        this.price = price;
        this.header_image = header_image;

    }
}
