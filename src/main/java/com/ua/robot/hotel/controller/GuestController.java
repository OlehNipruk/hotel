package com.ua.robot.hotel.controller;

import com.ua.robot.hotel.dto.GuestDto;
import com.ua.robot.hotel.dto.RoomDto;
import com.ua.robot.hotel.model.Guest;
import com.ua.robot.hotel.model.Room;
import com.ua.robot.hotel.service.GuestService;
import com.ua.robot.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<String> addGuest(@RequestBody Guest guest) {
        guestService.addGuest(guest);
        return ResponseEntity.ok("Guest created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGuest(@PathVariable("id") Long id) {
        guestService.deleteGuest(id);
        return ResponseEntity.ok("Guest deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateGuest(@PathVariable("id") Long id, @RequestBody Guest guest) {
        guestService.updateGuest(id, guest);
        return ResponseEntity.ok("Guest updated successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestDto> getGustById(@PathVariable("id") Long id) {
        Optional<Guest> guestOptional = guestService.getGuestById(id);
        if (guestOptional.isPresent()) {
            Guest guest = guestOptional.get();
            GuestDto guestDto = convertToGuestDto(guest);
            return ResponseEntity.ok(guestDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/guests/searchByPassport")
    public ResponseEntity<GuestDto> searchGuestByPassportNumber(@RequestParam("passportNumber") String passportNumber) {
        Optional<Guest> guestOptional = guestService.getGuestByPassportNumber(passportNumber);
        Guest guest = guestOptional.orElse(null);
        if (guest == null) {
            return ResponseEntity.notFound().build();
        }
        GuestDto guestDto = convertToGuestDto(guest);
        return ResponseEntity.ok(guestDto);
    }

    @GetMapping("/guests/searchByLastName")
    public ResponseEntity<List<GuestDto>> searchGuestByLastName(@RequestParam("lastName") String lastName) {
        Optional<Guest> guests = guestService.searchGuestByLastName(lastName);

        if (guests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<GuestDto> guestDtoList = guests.stream()
                .map(this::convertToGuestDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(guestDtoList);
    }
    @PostMapping("/guests/{guestId}/rooms/{roomId}")
    public ResponseEntity<String> addGuestToRoom(
            @PathVariable Long guestId,
            @PathVariable Long roomId,
            @RequestBody GuestDto guestDto
    ) {
        Guest guest = guestService.getGuestById(guestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found"));
        Room room = roomService.getRoomById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
        Guest updatedGuest = convertToGuestEntity(guestDto);
        updatedGuest.setId(guest.getId());
        updatedGuest.setRoom(room);

        guestService.addGuest(updatedGuest);

        return ResponseEntity.ok("Guest successfully added to room");
    }

    @PutMapping("/guests/{guestId}/rooms/{roomId}")
    public ResponseEntity<String> moveGuestToRoom(
            @PathVariable Long guestId,
            @PathVariable Long roomId
    ) {
        Guest guest = guestService.getGuestById(guestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found"));
        Room newRoom = roomService.getRoomById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "New room not found"));

        guest.setRoom(newRoom);
        guestService.addGuest(guest);

        return ResponseEntity.ok("Guest successfully moved to new room");
    }

    private GuestDto convertToGuestDto(Guest guest) {
        GuestDto guestDto = new GuestDto();
        guestDto.setId(guest.getId());
        guestDto.setFirstName(guest.getFirstName());
        guestDto.setLastName(guest.getLastName());
        guestDto.setPassportNumber(guest.getPassportNumber());
        return guestDto;
    }

    private Guest convertToGuestEntity(GuestDto guestDto) {
        Guest guest = new Guest();
        guest.setId(guestDto.getId());
        guest.setFirstName(guestDto.getFirstName());
        guest.setLastName(guestDto.getLastName());
        guest.setPassportNumber(guestDto.getPassportNumber());
        return guest;
    }

}