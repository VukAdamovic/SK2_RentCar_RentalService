package com.example.SK_Project2.RentalCarService.dto.type;

import javax.validation.constraints.NotBlank;

public class TypeCreateDto {

    @NotBlank
    private String name;

    public TypeCreateDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
