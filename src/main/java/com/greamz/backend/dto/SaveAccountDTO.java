package com.greamz.backend.dto;


import com.greamz.backend.annotations.UniqueEmail;
import com.greamz.backend.annotations.UsernameUnique;
import com.greamz.backend.model.Authority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SaveAccountDTO {



    private String username;
    private String password;
    @NotBlank(message = "Họ tên đang để trống")
    private String fullname;
    @NotBlank(message = "Email đang để trống")
    @Email(message = "Email không đúng định dạng")
    @UniqueEmail
    private String email;
    private String photo;
    private boolean isEnabled;

    private List<Authority> authorities;
}
