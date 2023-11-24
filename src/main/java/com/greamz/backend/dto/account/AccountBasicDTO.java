package com.greamz.backend.dto.account;

import com.greamz.backend.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountBasicDTO {
    private Integer id;
    private String username;
    private String email;
    private String fullname;
    private String photo;
    private Role role;

}
