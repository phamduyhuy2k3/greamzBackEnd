package com.greamz.backend.dto;

import com.greamz.backend.validation.annotations.PasswordValueMatch;
import com.greamz.backend.validation.annotations.UniqueEmail;
import com.greamz.backend.validation.annotations.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Không trùng password"
        )
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileImportant {
    private Integer id;
    @UniqueEmail
    private String email;
    @ValidPassword
    @NotBlank(message = "Password đang để trống")
    private String password;
    @ValidPassword
    @NotBlank(message = "Xác nhận password đang để trống")
    private String confirmPassword;
}
