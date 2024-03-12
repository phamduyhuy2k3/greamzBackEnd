package com.greamz.backend.dto.game;

import com.greamz.backend.dto.platform.PlatformBasicDTO;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GameCart {
    private Long appid;
    private String name;
    private Double price;
    private String header_image;
    private Integer discount;

}
