package com.greamz.backend.dto.account;

import com.greamz.backend.validation.annotations.PasswordValueMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "newEmail",
                fieldMatch = "confirmNewEmail",
                message = "Please confirm your password"
        )
})
public class ChangeEmail {
    @NotBlank(message = "Password đang để trống")
    private String currentPassword;
    @Email(message = "Email không đúng định dạng")
    @NotBlank(message = "Email đang để trống")
    private String newEmail;
    @Email(message = "Email không đúng định dạng")
    @NotBlank(message = "Xác nhận email đang để trống")
    private String confirmNewEmail;
}
