package com.example.SK_Project2.RentalCarService.dto.model;

import javax.validation.constraints.NotBlank;

public class ModelCreateDto {

    @NotBlank
    private String name;

    public ModelCreateDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
