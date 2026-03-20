package edu.ntt.hotelbookingplatform.service;

import edu.ntt.hotelbookingplatform.exception.RoomAlreadyExistsException;
import edu.ntt.hotelbookingplatform.exception.RoomNotFoundException;
import edu.ntt.hotelbookingplatform.model.Room;
import edu.ntt.hotelbookingplatform.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository repo;

    @InjectMocks
    private RoomService service;

    private Room room() {
        Room r = new Room();
        r.setId(1L);
        r.setRoomNumber("101");
        r.setType("Deluxe");
        r.setCapacity(2);
        r.setPricePerNight(200.0);
        return r;
    }

    // CREATE
    @Test
    void create_ok() {
        Room r = room();

        when(repo.existsByRoomNumber("101")).thenReturn(false);
        when(repo.save(r)).thenReturn(r);

        Room result = service.createRoom(r);

        assertNotNull(result);
        assertEquals("101", result.getRoomNumber());
        verify(repo).save(r);
    }

    @Test
    void create_fail_exists() {
        Room r = room();

        when(repo.existsByRoomNumber("101")).thenReturn(true);

        assertThrows(RoomAlreadyExistsException.class,
                () -> service.createRoom(r));

        verify(repo, never()).save(any());
    }

    // GET
    @Test
    void get_ok() {
        Room r = room();

        when(repo.findById(1L)).thenReturn(Optional.of(r));

        Room result = service.getRoomById(1L);

        assertEquals("101", result.getRoomNumber());
    }

    @Test
    void get_fail_notFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class,
                () -> service.getRoomById(1L));
    }

    // UPDATE
    @Test
    void update_ok() {
        Room existing = room();
        Room updated = room();
        updated.setRoomNumber("102");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.existsByRoomNumber("102")).thenReturn(false);
        when(repo.save(existing)).thenReturn(existing);

        Room result = service.updateRoom(1L, updated);

        assertEquals("102", result.getRoomNumber());
        verify(repo).save(existing);
    }

    @Test
    void update_fail_notFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class,
                () -> service.updateRoom(1L, room()));
    }

    @Test
    void update_fail_duplicate() {
        Room existing = room();
        Room updated = room();
        updated.setRoomNumber("102");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.existsByRoomNumber("102")).thenReturn(true);

        assertThrows(RoomAlreadyExistsException.class,
                () -> service.updateRoom(1L, updated));
    }

    // DELETE
    @Test
    void delete_ok() {
        when(repo.existsById(1L)).thenReturn(true);

        service.deleteRoom(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    void delete_fail_notFound() {
        when(repo.existsById(1L)).thenReturn(false);

        assertThrows(RoomNotFoundException.class,
                () -> service.deleteRoom(1L));
    }


    @Test
    void getAll_ok() {
        when(repo.findAll()).thenReturn(List.of(room()));

        var result = service.getAllRooms();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }

}
