package edu.ntt.hotelbookingplatform.controller;

import edu.ntt.hotelbookingplatform.dto.in.UserCreationDTO;
import edu.ntt.hotelbookingplatform.dto.mapper.UserMapper;
import edu.ntt.hotelbookingplatform.dto.out.UserDTO;
import edu.ntt.hotelbookingplatform.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/")
@Validated
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping()
    public List<UserDTO> getUsers(){
        return userService.getAllUsers().stream().map(userMapper::toDto).toList();
    }

    @GetMapping("/{email}")
    public UserDTO getUser(@PathVariable @Email(message="Email must have a valid format!") String email){
        return userMapper.toDto(userService.getUserByEmail(email));
    }

    @PostMapping()
    public void createUser(@Valid @RequestBody UserCreationDTO userCreationDTO){
        userService.createUser(userMapper.toUser(userCreationDTO));
    }

    @DeleteMapping("/{email}")
    public void deleteUser(@PathVariable @Email(message="Email must have a valid format!")  String email){
        userService.deleteUser(email);
    }
}
