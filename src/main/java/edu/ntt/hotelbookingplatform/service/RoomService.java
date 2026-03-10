package edu.ntt.hotelbookingplatform.service;


import edu.ntt.hotelbookingplatform.model.Room;
import edu.ntt.hotelbookingplatform.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow();
    }

    public Room createRoom(Room room){
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id,Room room){
        room.setId(id);
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id){
        roomRepository.deleteById(id);
    }






}
