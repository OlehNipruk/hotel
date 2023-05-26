package com.ua.robot.hotel.service;

import com.ua.robot.hotel.model.Reservation;
import com.ua.robot.hotel.model.Room;
import com.ua.robot.hotel.repository.ReservationRepository;
import com.ua.robot.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;
    public void addReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public Reservation reserveRoom(Long roomId, Reservation reservation) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            reservation.setRoom(room);
            return reservationRepository.save(reservation);
        }
        return null;
    }
}