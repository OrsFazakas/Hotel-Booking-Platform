package edu.ntt.hotelbookingplatform.controller;

import edu.ntt.hotelbookingplatform.dto.in.ChangeNameDTO;
import edu.ntt.hotelbookingplatform.dto.in.ChangePasswordDTO;
import edu.ntt.hotelbookingplatform.dto.in.ChangeRoleDTO;
import edu.ntt.hotelbookingplatform.dto.in.UserCreationDTO;
import edu.ntt.hotelbookingplatform.dto.mapper.UserMapper;
import edu.ntt.hotelbookingplatform.dto.out.UserDTO;
import edu.ntt.hotelbookingplatform.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<String> createUser(@Valid @RequestBody UserCreationDTO userCreationDTO){
        userService.createUser(userMapper.toUser(userCreationDTO));
        return ResponseEntity.ok("User account created successfully!");
    }

    @PatchMapping("/changeRole")
    public ResponseEntity<String> changeUserRole(@Valid @RequestBody ChangeRoleDTO changeRoleDTO){
        userService.updateUserRole(changeRoleDTO.getEmail(), changeRoleDTO.getRole());
        return ResponseEntity.ok("User role changed successfully!");
    }

    @PatchMapping("/changeName")
    public ResponseEntity<String> changeUserName(@Valid @RequestBody ChangeNameDTO changeNameDTO, @AuthenticationPrincipal String currentUserEmail){
        userService.updateUserName(currentUserEmail, changeNameDTO.getFirstName(), changeNameDTO.getLastName());
        return ResponseEntity.ok("User name changed successfully!");
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<String> changeUserPassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO, @AuthenticationPrincipal String currentUserEmail){
        userService.updateUserPassword(currentUserEmail, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        return ResponseEntity.ok("User password changed successfully!");
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable @Email(message="Email must have a valid format!")  String email){
        userService.deleteUser(email);
        return ResponseEntity.ok("User account deleted successfully!");
    }
}
