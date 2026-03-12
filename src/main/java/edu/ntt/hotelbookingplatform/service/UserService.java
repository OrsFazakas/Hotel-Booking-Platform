package edu.ntt.hotelbookingplatform.service;

import edu.ntt.hotelbookingplatform.exception.UserAlreadyExistsException;
import edu.ntt.hotelbookingplatform.exception.UserNotFoundException;
import edu.ntt.hotelbookingplatform.model.Users;
import edu.ntt.hotelbookingplatform.repository.UserRepository;
import edu.ntt.hotelbookingplatform.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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


}
