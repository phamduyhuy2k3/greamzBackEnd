package com.greamz.backend.dto.platform;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlatformDTO {

    private Integer id;
    private String name;
    private Long stock;
    private boolean isOutOfStock;
    public PlatformDTO(Integer id, String name, Long stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.isOutOfStock = stock == 0;
    }



}
