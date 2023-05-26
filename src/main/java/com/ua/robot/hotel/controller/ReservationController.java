package com.ua.robot.hotel.controller;

import com.ua.robot.hotel.dto.ReservationDto;
import com.ua.robot.hotel.model.Guest;
import com.ua.robot.hotel.model.Reservation;
import com.ua.robot.hotel.model.Room;
import com.ua.robot.hotel.service.GuestService;
import com.ua.robot.hotel.service.ReservationService;
import com.ua.robot.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final RoomService roomService;
    private final GuestService guestService;

    @PostMapping("/rooms/{roomId}/reservations")
    public ResponseEntity<String> reserveRoom(
            @PathVariable Long roomId,
            @RequestBody ReservationDto reservationDto
    ) {
        Optional<Room> room = roomService.getRoomById(roomId);
        if (room.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reservation reservation = convertToReservationEntity(reservationDto);
        reservation.setRoom(room.get());
        reservationService.addReservation(reservation);
        return ResponseEntity.ok("Room successfully reserved");
    }

    private ReservationDto convertToReservationDto(Reservation reservation) {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservation.getId());
        reservationDto.setGuestIds(reservation.getGuests().stream().map(Guest::getId).collect(Collectors.toList()));
        reservationDto.setRoomId(reservation.getRoom().getId());
        reservationDto.setStartDate(reservation.getStartDate());
        reservationDto.setEndDate(reservation.getEndDate());

        return reservationDto;
    }

    private Reservation convertToReservationEntity(ReservationDto reservationDto) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationDto.getId());

        Optional<Room> room = roomService.getRoomById(reservationDto.getRoomId());
        if (room.isPresent()) {
            reservation.setRoom(room.get());
        } else {
            return null;
        }
        List<Guest> guests = reservationDto.getGuestIds().stream()
                .map(guestId -> guestService.getGuestById(guestId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        reservation.setGuests(guests);

        reservation.setStartDate(reservationDto.getStartDate());
        reservation.setEndDate(reservationDto.getEndDate());
        reservation.setId(reservationDto.getId());

        return reservation;
    }
}