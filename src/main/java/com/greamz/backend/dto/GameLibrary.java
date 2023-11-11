package com.greamz.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameLibrary {
    private Long appid;
    private BigDecimal totalQuantity;
    private String name;
    private String header_image;
}
