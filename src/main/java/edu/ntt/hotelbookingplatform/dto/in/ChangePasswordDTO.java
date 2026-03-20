package edu.ntt.hotelbookingplatform.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {
    @NotBlank(message = "Password field must not be blank!")
    @Size(min = 8, message = "Password must be at least 8 characters long!")
    private String oldPassword;

    @NotBlank(message = "Password field must not be blank!")
    @Size(min = 8, message = "Password must be at least 8 characters long!")
    private String newPassword;
}
