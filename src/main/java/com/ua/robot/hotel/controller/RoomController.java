package com.ua.robot.hotel.controller;

import com.ua.robot.hotel.dto.RoomDto;
import com.ua.robot.hotel.model.Room;
import com.ua.robot.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<String> addRoom(@RequestBody Room room) {
        roomService.addRoom(room);
        return ResponseEntity.ok("Room created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable("id") Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateRoom(@PathVariable("id") Long id, @RequestBody Room room) {
        roomService.updateRoom(id, room);
        return ResponseEntity.ok("Room updated successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable("id") Long id) {
        Optional<Room> roomOptional = roomService.getRoomById(id);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            RoomDto roomDto = convertToRoomDto(room);
            return ResponseEntity.ok(roomDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomDto> roomDtoList = rooms.stream()
                .map(this::convertToRoomDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roomDtoList);
    }
    @GetMapping("/rooms/available")
    public ResponseEntity<List<RoomDto>> searchAvailableRooms(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate
    ) {
        List<Room> availableRooms = roomService.getAvailableRooms(startDate, endDate);
        List<RoomDto> roomDtos = availableRooms.stream()
                .map(this::convertToRoomDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roomDtos);
    }

    public RoomDto convertToRoomDto(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setRoomNumber(room.getRoomNumber());
        return roomDto;
    }
}
