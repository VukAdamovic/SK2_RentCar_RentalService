package com.example.SK_Project2.RentalCarService.mapper;

import com.example.SK_Project2.RentalCarService.domain.Reservation;
import com.example.SK_Project2.RentalCarService.dto.reservation.ReservationCreateDto;
import com.example.SK_Project2.RentalCarService.dto.reservation.ReservationDto;

import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationMapper() {
    }

    public ReservationDto resevationToReservationDto(Reservation reservation){
        ReservationDto reservationDto = new ReservationDto();

        reservationDto.setId(reservation.getId());
        reservationDto.setUserId(reservation.getUserId());
        reservationDto.setCarId(reservation.getCarId());
        reservationDto.setStartDate(reservation.getStartDate());
        reservationDto.setEndDate(reservation.getEndDate());
        reservationDto.setTotalPrice(reservation.getTotalPrice());

        return  reservationDto;
    }

    public Reservation reservationCreateDtoToReservation(ReservationCreateDto reservationCreateDto){
        Reservation reservation = new Reservation();

        //user id cu setovati liniju nakon sto pozovem ovu metodu
        reservation.setCarId(reservationCreateDto.getCarId());
        reservation.setStartDate(reservationCreateDto.getStartDate());
        reservation.setEndDate(reservationCreateDto.getEndDate());
        reservation.setTotalPrice(0);

        return  reservation;
    }
}
