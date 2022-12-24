package com.example.SK_Project2.RentalCarService.dto.company;

import com.example.SK_Project2.RentalCarService.dto.car.CarDto;

import java.util.List;

public class CompanyDto {
    private Long id;
    private String name;
    private String description;
    private Integer numOfCars;
    private String city;
    private List<CarDto> carList;

    public CompanyDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumOfCars() {
        return numOfCars;
    }

    public void setNumOfCars(Integer numOfCars) {
        this.numOfCars = numOfCars;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<CarDto> getCarList() {
        return carList;
    }

    public void setCarList(List<CarDto> carList) {
        this.carList = carList;
    }
}
