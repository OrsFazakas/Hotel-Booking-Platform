package edu.ntt.hotelbookingplatform.service;

import edu.ntt.hotelbookingplatform.exception.UserWrongPasswordException;
import edu.ntt.hotelbookingplatform.model.Users;
import edu.ntt.hotelbookingplatform.service.interfaces.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    @Autowired
    public AuthService(UserService userService, BCryptPasswordEncoder passwordEncoder, JWTService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public String login(String email, String password) {
        Users user = userService.getUserByEmail(email);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserWrongPasswordException();
        }

        return jwtService.generateToken(user);
    }


}
