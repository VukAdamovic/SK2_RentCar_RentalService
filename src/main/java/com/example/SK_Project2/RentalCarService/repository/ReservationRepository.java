package com.example.SK_Project2.RentalCarService.repository;

import com.example.SK_Project2.RentalCarService.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
