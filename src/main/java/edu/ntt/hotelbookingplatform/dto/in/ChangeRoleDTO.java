package edu.ntt.hotelbookingplatform.dto.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRoleDTO {
    @NotBlank(message = "Email field must not be blank!")
    @Email(message="Email must have a valid format!")
    private String email;


    @NotBlank(message = "Role field must not be blank!")
    @Pattern(regexp = "(Admin|Customer)", message = "Role field must be filled with one of two roles: Customer or Admin!")
    private String role;
}
