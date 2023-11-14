package com.greamz.backend.dto;

import com.greamz.backend.enumeration.Devices;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameFilter {
    private List<Long> categoriesId;
    private Long platformId;
    private int page;
    private int size;
    private Devices devices;
    private Double minPrice;
    private Double maxPrice;
    private SortFilter sort;

}
