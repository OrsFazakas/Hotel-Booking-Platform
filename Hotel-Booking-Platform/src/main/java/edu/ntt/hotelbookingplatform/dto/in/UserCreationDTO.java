package edu.ntt.hotelbookingplatform.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCreationDTO {
    @NotBlank(message = "Email field must not be blank!")
    @Email(message="Email must have a valid format!")
    private String email;

    @NotBlank(message = "Password field must not be blank!")
    @Size(min = 8, message = "Password must be at least 8 characters long!")
    private String password;

    @NotBlank(message = "First name field must not be blank!")
    private String firstName;

    @NotBlank(message = "Last name field must not be blank!")
    private String lastName;

    @NotBlank(message = "Role field must not be blank!")
    @Pattern(regexp = "(Admin|Customer)", message = "Role field must be filled with one of two roles: Customer or Admin!")
    private String role;
}
