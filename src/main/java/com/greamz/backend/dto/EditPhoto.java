package com.greamz.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditPhoto {
    private Integer id;
    @NotBlank(message = "Photo is required")
    private String photo;
}
