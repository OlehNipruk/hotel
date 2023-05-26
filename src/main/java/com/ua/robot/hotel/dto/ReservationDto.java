package com.ua.robot.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private Long id;
    private Long roomId;
    private List<Long> guestIds;
    private LocalDate startDate;
    private LocalDate endDate;
}
