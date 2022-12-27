package com.example.SK_Project2.RentalCarService.mapper;

import com.example.SK_Project2.RentalCarService.domain.Car;
import com.example.SK_Project2.RentalCarService.domain.Reservation;
import com.example.SK_Project2.RentalCarService.dto.reservation.ReservationCreateDto;
import com.example.SK_Project2.RentalCarService.dto.reservation.ReservationDto;

import com.example.SK_Project2.RentalCarService.exception.NotFoundException;
import com.example.SK_Project2.RentalCarService.repository.CarRepository;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    private CarRepository carRepository;

    public ReservationMapper(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public ReservationDto resevationToReservationDto(Reservation reservation){
        ReservationDto reservationDto = new ReservationDto();

        Car car = carRepository.findById(reservation.getCar().getId())
                .orElseThrow(() -> new NotFoundException(String.format("Car with id: %d does not exists.", reservation.getCar().getId())));

        reservationDto.setId(reservation.getId());
        reservationDto.setUserId(reservation.getUserId());
        reservationDto.setCarId(reservation.getCar().getId());
        reservationDto.setCarName(car.getModel().getName() + " " + car.getType().getName());
        reservationDto.setStartDate(reservation.getStartDate());
        reservationDto.setEndDate(reservation.getEndDate());
        reservationDto.setTotalPrice(reservation.getTotalPrice());
        reservationDto.setEmail(reservation.getEmail());

        return  reservationDto;
    }

    public Reservation reservationCreateDtoToReservation(ReservationCreateDto reservationCreateDto){
        Reservation reservation = new Reservation();


        Car car = carRepository.findById(reservationCreateDto.getCarId())
                .orElseThrow(() -> new NotFoundException(String.format("Car with id: %d does not exists.", reservationCreateDto.getCarId())));

        //user id cu setovati liniju nakon sto pozovem ovu metodu
        reservation.setCar(car);
        reservation.setStartDate(reservationCreateDto.getStartDate());
        reservation.setEndDate(reservationCreateDto.getEndDate());
        reservation.setTotalPrice(0); //setujem cenu svakako posle
        reservation.setEmail(null); // setuje email prilikom kreiranja rezervacije
        reservation.setThreeDaysReminder("NOT_SEND");

        return  reservation;
    }
}
