package edu.ntt.hotelbookingplatform.controller;

import edu.ntt.hotelbookingplatform.dto.in.RoomCreationDTO;
import edu.ntt.hotelbookingplatform.dto.mapper.RoomMapper;
import edu.ntt.hotelbookingplatform.dto.out.RoomDTO;
import edu.ntt.hotelbookingplatform.model.Room;
import edu.ntt.hotelbookingplatform.service.RoomService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Mock
    private RoomService service;

    @Mock
    private RoomMapper mapper;

    @InjectMocks
    private RoomController controller;

    private Room room() {
        Room r = new Room();
        r.setId(1L);
        r.setRoomNumber("101");
        return r;
    }

    private RoomDTO dto() {
        RoomDTO d = new RoomDTO();
        d.setRoomNumber("101");
        return d;
    }

    private RoomCreationDTO createDto() {
        RoomCreationDTO d = new RoomCreationDTO();
        d.setRoomNumber("101");
        d.setType("Deluxe");
        d.setCapacity(2);
        d.setPricePerNight(200.0);
        return d;
    }

    // GET ALL
    @Test
    void getAll_ok() {
        when(service.getAllRooms()).thenReturn(List.of(room()));
        when(mapper.toDto(any())).thenReturn(dto());

        List<RoomDTO> result = controller.getRooms();

        assertEquals(1, result.size());
        assertEquals("101", result.get(0).getRoomNumber());
    }

    // GET BY ID
    @Test
    void getById_ok() {
        when(service.getRoomById(1L)).thenReturn(room());
        when(mapper.toDto(any())).thenReturn(dto());

        RoomDTO result = controller.getRoom(1L);

        assertEquals("101", result.getRoomNumber());
    }

    // CREATE
    @Test
    void create_ok() {
        when(mapper.toRoom(any())).thenReturn(room());

        String result = controller.createRoom(createDto());

        assertEquals("Room created successfully!", result);
        verify(service).createRoom(any());
    }

    // UPDATE
    @Test
    void update_ok() {
        when(mapper.toRoom(any())).thenReturn(room());

        String result = controller.updateRoom(1L, createDto());

        assertEquals("Room updated successfully!", result);
        verify(service).updateRoom(eq(1L), any());
    }

    // DELETE
    @Test
    void delete_ok() {
        String result = controller.deleteRoom(1L);

        assertEquals("Room deleted successfully!", result);
        verify(service).deleteRoom(1L);
    }
}