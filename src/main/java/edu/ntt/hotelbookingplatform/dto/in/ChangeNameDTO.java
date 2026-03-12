package edu.ntt.hotelbookingplatform.dto.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeNameDTO {
    @NotBlank(message = "First name field must not be blank!")
    private String firstName;


    @NotBlank(message = "Last name field must not be blank!")
    private String lastName;
}
