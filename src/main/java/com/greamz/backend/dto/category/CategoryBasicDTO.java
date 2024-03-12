package com.greamz.backend.dto.category;

import com.greamz.backend.enumeration.CategoryTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBasicDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private Long id;
    private String name;
    private CategoryTypes categoryTypes;
    private Long gameCount;

}