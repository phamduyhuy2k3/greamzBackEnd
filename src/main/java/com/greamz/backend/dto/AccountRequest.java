package com.greamz.backend.dto;

import com.greamz.backend.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequest {
    private String username;
    private String password;
    private String email;
    private String fullname;
    private String photo;
    private Role role;

}
