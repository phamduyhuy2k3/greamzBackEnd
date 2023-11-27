package com.greamz.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopSellingProduct {
    @Id
    private Long appId;

    private String name;
    private String headerImage;
    private String website;
    private BigDecimal price;
    private Integer totalQuantitySold;
}
