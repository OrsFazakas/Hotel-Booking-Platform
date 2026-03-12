package edu.ntt.hotelbookingplatform.dto.mapper;

import edu.ntt.hotelbookingplatform.dto.in.UserCreationDTO;
import edu.ntt.hotelbookingplatform.dto.out.UserDTO;
import edu.ntt.hotelbookingplatform.model.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDto(Users user){
        return new UserDTO(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }

    public Users toUser(UserCreationDTO userCreationDTO){
        return new Users(
                userCreationDTO.getEmail(),
                userCreationDTO.getPassword(),
                userCreationDTO.getFirstName(),
                userCreationDTO.getLastName(),
                userCreationDTO.getRole()
        );
    }

}
