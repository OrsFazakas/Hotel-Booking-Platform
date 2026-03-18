package edu.ntt.hotelbookingplatform.service;

import edu.ntt.hotelbookingplatform.exception.RoomAlreadyExistsException;
import edu.ntt.hotelbookingplatform.exception.RoomNotFoundException;
import edu.ntt.hotelbookingplatform.model.Room;
import edu.ntt.hotelbookingplatform.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
    }

    public Room createRoom(Room room) {

        if(roomRepository.existsByRoomNumber(room.getRoomNumber())){
            throw new RoomAlreadyExistsException(room.getRoomNumber());
        }

        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, Room room) {

        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));

        if(!existingRoom.getRoomNumber().equals(room.getRoomNumber()) &&
                roomRepository.existsByRoomNumber(room.getRoomNumber())) {
            throw new RoomAlreadyExistsException(room.getRoomNumber());
        }

        existingRoom.setRoomNumber(room.getRoomNumber());
        existingRoom.setType(room.getType());
        existingRoom.setCapacity(room.getCapacity());
        existingRoom.setPricePerNight(room.getPricePerNight());
        existingRoom.setFeatures(room.getFeatures());

        return roomRepository.save(existingRoom);
    }

    public void deleteRoom(Long id) {

        if (!roomRepository.existsById(id)) {
            throw new RoomNotFoundException(id);
        }
        roomRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut, Integer capacity, String type) {
        if (!checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }

        return roomRepository.findAvailableRoomsFiltered(checkIn, checkOut, capacity, type);
    }
}
