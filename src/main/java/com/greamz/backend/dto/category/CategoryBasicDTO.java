package com.greamz.backend.dto.category;

import com.greamz.backend.enumeration.CategoryTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBasicDTO {
    private Long id;
    private String name;
    private CategoryTypes categoryTypes;
    private Long gameCount;

}