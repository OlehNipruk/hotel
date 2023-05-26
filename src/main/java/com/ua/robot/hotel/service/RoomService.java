package com.ua.robot.hotel.service;

import com.ua.robot.hotel.model.Room;
import com.ua.robot.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    public void addRoom(Room room) {
        roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
    public void updateRoom(Long id, Room updatedroom) {
        updatedroom.setId(id);
        roomRepository.save(updatedroom);
    }
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

}
