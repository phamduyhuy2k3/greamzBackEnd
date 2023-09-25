package com.greamz.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserProfileDTO {
    private String username;
    private String email;
    private String photo;
    private String fullname;
    private Collection<? extends GrantedAuthority> authorities;
}
