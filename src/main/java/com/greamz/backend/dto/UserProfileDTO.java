package com.greamz.backend.dto;


import com.greamz.backend.enumeration.AuthProvider;
import com.greamz.backend.enumeration.Role;
import com.greamz.backend.validation.annotations.UniqueEmail;
import com.greamz.backend.validation.annotations.UsernameUnique;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserProfileDTO  {
    private Integer id;
    @UsernameUnique
    private String username;
    @UniqueEmail
    private String email;
    private String photo;
    private String fullname;
    private Role role;
    private Date createdAt;
    private AuthProvider provider;

}
