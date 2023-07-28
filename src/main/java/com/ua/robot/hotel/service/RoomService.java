package com.ua.robot.hotel.service;

import com.ua.robot.hotel.model.Room;
import com.ua.robot.hotel.repository.ReservationRepository;
import com.ua.robot.hotel.repository.RoomRepository;
import com.ua.robot.hotel.model.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
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

    public List<Room> getAvailableRooms(LocalDate startDate, LocalDate endDate) {
        List<Reservation> reservations = reservationRepository.findByDates(startDate, endDate);
        List<Long> bookedRoomIds = reservations.stream()
                .map(reservation -> reservation.getRoom().getId())
                .collect(Collectors.toList());
        return roomRepository.findAvailableRooms(bookedRoomIds);
    }



}
