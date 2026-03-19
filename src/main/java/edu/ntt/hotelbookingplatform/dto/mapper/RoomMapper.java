package edu.ntt.hotelbookingplatform.dto.mapper;

import edu.ntt.hotelbookingplatform.dto.in.RoomCreationDTO;
import edu.ntt.hotelbookingplatform.dto.out.RoomDTO;
import edu.ntt.hotelbookingplatform.model.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public RoomDTO toDto(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setRoomNumber(room.getRoomNumber());
        dto.setType(room.getType());
        dto.setCapacity(room.getCapacity());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setFeatures(room.getFeatures());

        return dto;
    }

    public Room toRoom(RoomCreationDTO roomCreationDTO) {
        return Room.builder()
                .roomNumber(roomCreationDTO.getRoomNumber())
                .type(roomCreationDTO.getType())
                .capacity(roomCreationDTO.getCapacity())
                .pricePerNight(roomCreationDTO.getPricePerNight())
                .features(roomCreationDTO.getFeatures())
                .build();
    }

}
