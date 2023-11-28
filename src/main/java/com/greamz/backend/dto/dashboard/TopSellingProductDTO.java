package com.greamz.backend.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopSellingProductDTO {
    private Long appid;
    private String name;
    private String image;
    private String website;
    private Double price;
    private BigDecimal totalQuantitySold;

}
