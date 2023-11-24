package com.greamz.backend.dto.platform;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
@Builder
public class PlatformDTO {

    private Integer id;
    private String name;

    public PlatformDTO(Integer id, String name, Long stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    private Long stock;

}
