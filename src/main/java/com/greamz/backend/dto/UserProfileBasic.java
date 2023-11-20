package com.greamz.backend.dto;

import com.greamz.backend.validation.annotations.UsernameUnique;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileBasic {
    private Integer id;
    private String photo;
    @UsernameUnique
    private String username;
    private String fullname;
}
