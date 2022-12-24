package com.example.SK_Project2.RentalCarService.service;

import com.example.SK_Project2.RentalCarService.dto.car.CarCreateDto;
import com.example.SK_Project2.RentalCarService.dto.car.CarDto;

import java.util.Date;
import java.util.List;

public interface CarService {
    //manager ovo koristi
    CarDto add(CarCreateDto carCreateDto);

    Boolean delete(Long id);

    CarDto update(CarDto carDto);

    //------------------//
    List<CarDto> findAll();

    CarDto findById(Long id);

    List<CarDto> findByCustomParams(String city,String companyName,Date startDate, Date endDate);

    List<CarDto> sortByDayPriceASC();

    List<CarDto> sortByDayPriceDESC();





}
