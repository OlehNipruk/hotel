package com.ua.robot.hotel.service;

import com.ua.robot.hotel.model.Guest;
import com.ua.robot.hotel.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;
    public void addGuest(Guest guest) {
        guestRepository.save(guest);
    }
    public void deleteGuest(Long id) {
        guestRepository.deleteById(id);
    }
    public void updateGuest(Long id, Guest updatedguest) {
        updatedguest.setId(id);
        guestRepository.save(updatedguest);
    }
    public Optional<Guest> getGuestById(Long id) {
        return guestRepository.findById(id);
    }

    public Optional<Guest> getGuestByPassportNumber (String passportNumber) {
        return guestRepository.findByPassportNumber(passportNumber);
    }
    public Optional<Guest> searchGuestByLastName(String lastName) {
        return guestRepository.findByLastName(lastName);
    }
}