package edu.ntt.hotelbookingplatform.service.interfaces;

import edu.ntt.hotelbookingplatform.model.User;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();
    User getUserByEmail(String email);
    void createUser(User user);
    void deleteUser(String email);
    void updateUserRole(String userEmail, String newRole);
    void changePassword(String oldPassword, String newPassword, String email);
    void updateUserName(String firstName, String lastName, String email);
}
