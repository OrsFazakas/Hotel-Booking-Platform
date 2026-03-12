package edu.ntt.hotelbookingplatform.service;

import edu.ntt.hotelbookingplatform.exception.UserAlreadyExistsException;
import edu.ntt.hotelbookingplatform.exception.UserNotFoundException;
import edu.ntt.hotelbookingplatform.exception.UserWrongPasswordException;
import edu.ntt.hotelbookingplatform.model.Users;
import edu.ntt.hotelbookingplatform.repository.UserRepository;
import edu.ntt.hotelbookingplatform.service.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void createUser(Users user) {
        String userEmail = user.getEmail();
        if(userRepository.findByEmail(userEmail).isEmpty()){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
       else
           throw new UserAlreadyExistsException(userEmail);
    }

    @Override
    public void deleteUser(String email) {
        Users userToDelete = getUserByEmail(email);
        userRepository.delete(userToDelete);
    }

    @Override
    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public void updateUserRole(String userEmail, String newRole) {
        Users userToUpdate = getUserByEmail(userEmail);
        userToUpdate.setRole(newRole);
        userRepository.save(userToUpdate);
    }

    @Override
    public void updateUserName(String userEmail, String newFirstName, String newLastName) {
        Users userToUpdate = getUserByEmail(userEmail);
        userToUpdate.setFirstName(newFirstName);
        userToUpdate.setLastName(newLastName);
        userRepository.save(userToUpdate);
    }

    @Override
    public void updateUserPassword(String userEmail, String oldPassword, String newPassword) {
        Users userToUpdate = getUserByEmail(userEmail);
        if (!passwordEncoder.matches(oldPassword, userToUpdate.getPassword())) {
            throw new UserWrongPasswordException();
        }
        else{
            userToUpdate.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(userToUpdate);
        }
    }


}
