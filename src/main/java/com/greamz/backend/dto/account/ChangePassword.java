package com.greamz.backend.dto.account;

import com.greamz.backend.validation.annotations.PasswordValueMatch;
import com.greamz.backend.validation.annotations.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "newPassword",
                fieldMatch = "confirmPassword",
                message = "Please confirm your password"
        )
})
public class ChangePassword {
    @NotBlank(message = "Password đang để trống")
    private String oldPassword;
    @ValidPassword
    @NotBlank(message = "Password đang để trống")
    private String newPassword;
    @ValidPassword
    @NotBlank(message = "Xác nhận password đang để trống")
    private String confirmPassword;
}
