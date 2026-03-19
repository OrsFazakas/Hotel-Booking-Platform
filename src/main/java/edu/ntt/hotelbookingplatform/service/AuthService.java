package edu.ntt.hotelbookingplatform.service;

import edu.ntt.hotelbookingplatform.exception.user.UserWrongPasswordException;
import edu.ntt.hotelbookingplatform.model.Users;
import edu.ntt.hotelbookingplatform.service.interfaces.IAuthService;
import edu.ntt.hotelbookingplatform.service.interfaces.IJWTService;
import edu.ntt.hotelbookingplatform.service.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService implements IAuthService {

    private final IUserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final IJWTService jwtService;

    @Override
    public String login(String email, String password) {
        Users user = userService.getUserByEmail(email);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserWrongPasswordException();
        }

        return jwtService.generateToken(user);
    }


}
