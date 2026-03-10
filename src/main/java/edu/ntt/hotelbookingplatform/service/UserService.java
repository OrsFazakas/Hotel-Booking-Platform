package edu.ntt.hotelbookingplatform.service;

import edu.ntt.hotelbookingplatform.exception.UserNotFoundException;
import edu.ntt.hotelbookingplatform.exception.UserWrongPasswordException;
import edu.ntt.hotelbookingplatform.model.User;
import edu.ntt.hotelbookingplatform.repository.UserRepository;
import edu.ntt.hotelbookingplatform.service.interfaces.IUserService;
import edu.ntt.hotelbookingplatform.validator.UserServiceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserServiceValidator userServiceValidator = new UserServiceValidator();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String email) {
        User userToDelete = getUserByEmail(email);
        userRepository.delete(userToDelete);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public void updateUserRole(String userEmail, String newRole) {
        User userToUpdate = getUserByEmail(userEmail);
        userToUpdate.setRole(newRole);
        userRepository.save(userToUpdate);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String email) {
        User userToUpdate = getUserByEmail(email);
        if(!userServiceValidator.validatePassword(userToUpdate.getPassword(), oldPassword))
            throw new UserWrongPasswordException();
        userToUpdate.setPassword(newPassword);
        userRepository.save(userToUpdate);
    }

    @Override
    public void updateUserName(String firstName, String lastName, String email) {
        User userToUpdate = getUserByEmail(email);
        userToUpdate.setFirstName(firstName);
        userToUpdate.setLastName(lastName);
        userRepository.save(userToUpdate);
    }
}
