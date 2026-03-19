package edu.ntt.hotelbookingplatform.dto.mapper;

import edu.ntt.hotelbookingplatform.dto.in.RoomCreationDTO;
import edu.ntt.hotelbookingplatform.dto.out.RoomDTO;
import edu.ntt.hotelbookingplatform.model.Room;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomMapperTest {

    private final RoomMapper mapper = new RoomMapper();

    private Room room() {
        Room r = new Room();
        r.setRoomNumber("101");
        r.setType("Deluxe");
        r.setCapacity(2);
        r.setPricePerNight(200.0);
        r.setFeatures("Sea view");
        return r;
    }

    private RoomCreationDTO createDto() {
        RoomCreationDTO d = new RoomCreationDTO();
        d.setRoomNumber("101");
        d.setType("Deluxe");
        d.setCapacity(2);
        d.setPricePerNight(200.0);
        d.setFeatures("Sea view");
        return d;
    }

    // toDto
    @Test
    void toDto_ok() {
        Room room = room();

        RoomDTO dto = mapper.toDto(room);

        assertEquals("101", dto.getRoomNumber());
        assertEquals("Deluxe", dto.getType());
        assertEquals(2, dto.getCapacity());
        assertEquals(200.0, dto.getPricePerNight());
        assertEquals("Sea view", dto.getFeatures());
    }

    // toRoom
    @Test
    void toRoom_ok() {
        RoomCreationDTO input = createDto();

        Room room = mapper.toRoom(input);

        assertEquals("101", room.getRoomNumber());
        assertEquals("Deluxe", room.getType());
        assertEquals(2, room.getCapacity());
        assertEquals(200.0, room.getPricePerNight());
        assertEquals("Sea view", room.getFeatures());
    }
}