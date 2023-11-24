package com.greamz.backend.dto.code;

import com.greamz.backend.dto.game.GameBasicDTO;
import com.greamz.backend.dto.platform.PlatformBasicDTO;

public class CodeActiveBasicDTO {
    private Long id;
    private String code;
    private Boolean active;
    private PlatformBasicDTO platform;
    private GameBasicDTO game;
}
