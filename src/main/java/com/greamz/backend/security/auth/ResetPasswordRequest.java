package com.greamz.backend.security.auth;

import com.greamz.backend.validation.annotations.PasswordValueMatch;
import com.greamz.backend.validation.annotations.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Không trùng password"
        )
})
public class ResetPasswordRequest {
    @ValidPassword
    @NotBlank(message = "Password đang để trống")
    private String password;
    @ValidPassword
    @NotBlank(message = "Xác nhận password đang để trống")
    private String confirmPassword;
    @NotBlank(message = "Token đang để trống")
    private String token;
}
