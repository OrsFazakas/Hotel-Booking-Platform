package edu.ntt.hotelbookingplatform.service.interfaces;

import edu.ntt.hotelbookingplatform.model.Users;

public interface IJWTService {
    String generateToken(Users user);
    String extractEmail(String token);
    String extractRole(String token);
    boolean isTokenValid(String token);
}
