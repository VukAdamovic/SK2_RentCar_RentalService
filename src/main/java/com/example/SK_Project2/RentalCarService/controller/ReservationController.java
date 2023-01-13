package com.example.SK_Project2.RentalCarService.controller;

import com.example.SK_Project2.RentalCarService.dto.reservation.ReservationCreateDto;
import com.example.SK_Project2.RentalCarService.dto.reservation.ReservationDto;
import com.example.SK_Project2.RentalCarService.security.CheckSecurity;
import com.example.SK_Project2.RentalCarService.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rental/reservation")
public class ReservationController {

    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    @PostMapping
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    public ResponseEntity<ReservationDto> addReservation(@RequestHeader("authorization") String authorization, @RequestBody ReservationCreateDto reservationCreateDto){
        return new ResponseEntity<>(reservationService.addReservation(authorization,reservationCreateDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_CLIENT","ROLE_MANAGER"})
    public ResponseEntity<Boolean> cancelReservation(@RequestHeader("authorization") String authorization, @PathVariable("id") Long id){
        return new ResponseEntity<>(reservationService.canceleReservation(authorization,id),HttpStatus.OK);
    }

}
