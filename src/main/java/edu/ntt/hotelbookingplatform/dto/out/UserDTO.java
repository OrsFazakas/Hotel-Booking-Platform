package edu.ntt.hotelbookingplatform.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserDTO {

    private String email;

    private String firstName;

    private String lastName;

    private String role;
}
