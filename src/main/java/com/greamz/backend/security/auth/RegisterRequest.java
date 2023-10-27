package com.greamz.backend.security.auth;

import com.greamz.backend.model.Authority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PasswordValueMatch.List({
})
public class RegisterRequest {
    @NotBlank(message = "Họ tên đang để trống")
    private String fullname;
    @NotBlank(message = "Username đang để trống")
//    @UsernameUnique
    private String username;
    @NotBlank(message = "Email đang để trống")
    @Email(message = "Email không đúng định dạng")
//    @UniqueEmail
    private String email;
    @NotBlank(message = "Password đang để trống")
    private String password;
    @NotBlank(message = "Xác nhận password đang để trống")
    private String confirmPassword;
    private List<Authority> authorities;
    interface ValidationStepOne{

    }
    interface ValidationStepTwo{

    }
}
