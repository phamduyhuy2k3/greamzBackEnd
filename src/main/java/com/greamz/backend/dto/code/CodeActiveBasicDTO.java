package com.greamz.backend.dto.code;


import com.greamz.backend.dto.platform.PlatformBasicDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeActiveBasicDTO {
    private Long id;
    private String code;
    private Boolean active;
    private PlatformBasicDTO platform;
    private Long appid;
    private String name;
    private String header_image;

}
