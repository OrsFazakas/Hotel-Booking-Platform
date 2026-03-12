package edu.ntt.hotelbookingplatform.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    @NotBlank(message = "Email field must not be blank!")
    @Email(message="Email must have a valid format!")
    private String email;

    @NotBlank(message = "Password field must not be blank!")
    private String password;
}
