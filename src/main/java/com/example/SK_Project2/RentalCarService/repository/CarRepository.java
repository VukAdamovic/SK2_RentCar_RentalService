package com.example.SK_Project2.RentalCarService.repository;

import com.example.SK_Project2.RentalCarService.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {



}
