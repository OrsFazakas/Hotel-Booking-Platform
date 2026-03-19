package edu.ntt.hotelbookingplatform.controller;

import edu.ntt.hotelbookingplatform.dto.in.RoomCreationDTO;
import edu.ntt.hotelbookingplatform.dto.mapper.RoomMapper;
import edu.ntt.hotelbookingplatform.dto.out.RoomDTO;
import edu.ntt.hotelbookingplatform.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@Validated
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @Autowired
    public RoomController(RoomService roomService, RoomMapper roomMapper) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
    }

    @GetMapping()
    public List<RoomDTO> getRooms(){
        return roomService.getAllRooms()
                .stream()
                .map(roomMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public RoomDTO getRoom(@PathVariable Long id){
        return roomMapper.toDto(roomService.getRoomById(id));
    }

    @PostMapping()
    public String createRoom(@Valid @RequestBody RoomCreationDTO roomCreationDTO){
        roomService.createRoom(roomMapper.toRoom(roomCreationDTO));
        return "Room created successfully!";
    }

    @PutMapping("/{id}")
    public String updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomCreationDTO roomCreationDTO){

        roomService.updateRoom(id, roomMapper.toRoom(roomCreationDTO));
        return "Room updated successfully!";
    }

    @DeleteMapping("/{id}")
    public String deleteRoom(@PathVariable Long id){
        roomService.deleteRoom(id);
        return "Room deleted successfully!";
    }
}
