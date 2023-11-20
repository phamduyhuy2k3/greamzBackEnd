package com.greamz.backend.dto;

import com.greamz.backend.validation.annotations.UsernameUnique;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditUsername {
    private Integer id;
    @UsernameUnique
    @NotBlank(message = "Username are mandatory")
    private String username;
}
