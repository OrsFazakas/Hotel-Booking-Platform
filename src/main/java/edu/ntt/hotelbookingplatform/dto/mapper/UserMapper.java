package edu.ntt.hotelbookingplatform.dto.mapper;

import edu.ntt.hotelbookingplatform.dto.in.UserCreationDTO;
import edu.ntt.hotelbookingplatform.dto.out.UserDTO;
import edu.ntt.hotelbookingplatform.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDto(User user){
        return new UserDTO(
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }

    public User toUser(UserCreationDTO userCreationDTO){
        return new User(
                userCreationDTO.getEmail(),
                userCreationDTO.getPassword(),
                userCreationDTO.getFirstName(),
                userCreationDTO.getLastName(),
                userCreationDTO.getRole()
        );
    }

}
