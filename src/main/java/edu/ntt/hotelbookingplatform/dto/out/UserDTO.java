package edu.ntt.hotelbookingplatform.dto.out;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String role;

    public UserDTO(String email, String password, String firstName, String lastName, String role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}
