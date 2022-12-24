package com.example.SK_Project2.RentalCarService.dto.company;

import javax.validation.constraints.NotBlank;

public class CompanyCreateDto {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private Integer numOfCars;
    @NotBlank
    private String city;

    public CompanyCreateDto() {
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
}
