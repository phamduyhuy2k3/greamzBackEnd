package com.greamz.backend.checkout.momo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MomoItem {
    private String id;
    private String name;
    private String imageUrl;
    private Integer quantity;
    private Double price;
    private BigDecimal totalPrice;
}
