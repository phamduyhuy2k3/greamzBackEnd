package com.greamz.backend.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder

@NoArgsConstructor
public class TopSellingProductDTO implements Serializable {
    private Long appid;
    private String name;
    private String image;
    private String website;
    private Integer discount;
    private Double price;
    private Long totalQuantitySold;

    public TopSellingProductDTO(Long appid, String name, String image, String website, Integer discount, Double price, Long totalQuantitySold) {
        this.appid = appid;
        this.name = name;
        this.image = image;
        this.website = website;
        this.discount = discount;
        this.price = price;
        this.totalQuantitySold = totalQuantitySold;
    }
}
