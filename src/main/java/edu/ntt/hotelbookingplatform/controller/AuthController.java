package edu.ntt.hotelbookingplatform.controller;

import edu.ntt.hotelbookingplatform.dto.in.LoginDTO;
import edu.ntt.hotelbookingplatform.dto.in.UserCreationDTO;
import edu.ntt.hotelbookingplatform.dto.mapper.UserMapper;
import edu.ntt.hotelbookingplatform.service.AuthService;
import edu.ntt.hotelbookingplatform.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth/")
@Validated
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthService authService;

    @Autowired
    public AuthController(UserService userService, UserMapper userMapper, AuthService authService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginDTO loginDTO){
        return authService.login(loginDTO.getEmail(), loginDTO.getPassword());
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserCreationDTO userCreationDTO){
        userService.createUser(userMapper.toUser(userCreationDTO));
        return ResponseEntity.ok("Successfully registered!");
    }


}
