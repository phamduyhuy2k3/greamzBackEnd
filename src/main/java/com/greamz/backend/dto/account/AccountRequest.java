package com.greamz.backend.dto.account;

import com.greamz.backend.enumeration.Role;
import com.greamz.backend.validation.annotations.UniqueEmail;
import com.greamz.backend.validation.annotations.UsernameUnique;
import com.greamz.backend.validation.annotations.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequest {
    private Integer id;
    @NotBlank(message = "Họ tên đang để trống")
    private String fullname;
    @NotBlank(message = "Username đang để trống")
    @UsernameUnique
    private String username;
    @NotBlank(message = "Email đang để trống")
    @Email(message = "Email không đúng định dạng")
    @UniqueEmail
    private String email;
    @ValidPassword
    @NotBlank(message = "Password đang để trống")
    private String password;
    private Role role;
    private boolean isEnabled;

}
