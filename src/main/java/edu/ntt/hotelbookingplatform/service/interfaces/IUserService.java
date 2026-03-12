package edu.ntt.hotelbookingplatform.service.interfaces;

import edu.ntt.hotelbookingplatform.model.Users;

import java.util.List;

public interface IUserService {
    List<Users> getAllUsers();
    Users getUserByEmail(String email);
    void createUser(Users user);
    void deleteUser(String email);
    void updateUserRole(String userEmail, String newRole);
    void updateUserName(String userEmail, String newFirstName, String newLastName);
    void updateUserPassword(String userEmail, String oldPassword, String newPassword);
}
