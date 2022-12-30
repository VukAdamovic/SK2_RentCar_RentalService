package com.example.SK_Project2.RentalCarService.service;

import com.example.SK_Project2.RentalCarService.dto.reservation.ReservationCreateDto;
import com.example.SK_Project2.RentalCarService.dto.reservation.ReservationDto;

public interface ReservationService {

    ReservationDto addReservation(String authorization,ReservationCreateDto reservationCreateDto);

    Boolean canceleReservation(String authorization, Long id);

    
}
