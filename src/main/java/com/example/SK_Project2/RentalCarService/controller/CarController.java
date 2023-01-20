package com.example.SK_Project2.RentalCarService.controller;

import com.example.SK_Project2.RentalCarService.dto.car.CarCreateDto;
import com.example.SK_Project2.RentalCarService.dto.car.CarDto;
import com.example.SK_Project2.RentalCarService.dto.car.CarFilterDto;
import com.example.SK_Project2.RentalCarService.security.CheckSecurity;
import com.example.SK_Project2.RentalCarService.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rental/car")
public class CarController {

    private CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<List<CarDto>> getAllCars(@RequestHeader("authorization") String authorization){
        return new ResponseEntity<>(carService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<CarDto> getCarById(@RequestHeader("authorization") String authorization,@PathVariable("id") Long id){
        return new ResponseEntity<>(carService.findById(id),HttpStatus.OK);
    }

    @GetMapping("/sortASC")
    public ResponseEntity<List<CarDto>> sortASC(){
        return new ResponseEntity<>(carService.sortByDayPriceASC(),HttpStatus.OK);
    }

    @GetMapping("/sortDESC")
    public ResponseEntity<List<CarDto>> sortDESC(){
        return new ResponseEntity<>(carService.sortByDayPriceDESC(),HttpStatus.OK);
    }

    @PutMapping("/search")
    public ResponseEntity<List<CarDto>> filter(@RequestBody CarFilterDto carFilterDto){
        return new ResponseEntity<>(carService.findCarFilter(carFilterDto),HttpStatus.OK);
    }

    //---------------------

    @PostMapping("/registration")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<CarDto> registerCar(@RequestHeader("authorization") String authorization, @RequestBody CarCreateDto carCreateDto){
        return new ResponseEntity<>(carService.add(carCreateDto),HttpStatus.CREATED);
    }

    //---------------------

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<Boolean> deleteCar(@RequestHeader("authorization") String authorization,@PathVariable("id") Long id){
        return new ResponseEntity<>(carService.delete(id),HttpStatus.OK);
    }

    //---------------------

    @PutMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<CarDto> updateCar(@RequestHeader("authorization") String authorization,@RequestBody CarDto carDto){
        return new ResponseEntity<>(carService.update(carDto),HttpStatus.OK);
    }

}
