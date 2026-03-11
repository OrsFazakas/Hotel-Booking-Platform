package edu.ntt.hotelbookingplatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long userId;

    @Setter
    private String email;

    @Setter
    private String password;

    @Setter
    private String firstName;

    @Setter
    private String lastName;

    @Setter
    private String role;

    public User(String email, String password, String first_name, String last_name, String role) {
        this.email = email;
        this.password = password;
        this.firstName = first_name;
        this.lastName = last_name;
        this.role = role;
    }

    public User() {

    }


}
