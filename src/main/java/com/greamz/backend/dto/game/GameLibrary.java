package com.greamz.backend.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class GameLibrary {
    private Long appid;
    private Long totalQuantity;
    private String name;
    private String header_image;

    public GameLibrary(Long appid, Long totalQuantity, String name, String header_image) {
        this.appid = appid;
        this.totalQuantity = totalQuantity;
        this.name = name;
        this.header_image = header_image;
    }
}
