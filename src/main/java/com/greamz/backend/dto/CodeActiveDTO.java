package com.greamz.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeActiveDTO {
    private Long id;
    private String code;
    private boolean active;
    private GameBasicDTO game;
    private PlatformBasicDTO platform;
}
