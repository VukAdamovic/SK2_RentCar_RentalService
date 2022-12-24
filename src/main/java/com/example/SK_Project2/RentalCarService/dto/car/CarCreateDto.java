package com.example.SK_Project2.RentalCarService.dto.car;

import javax.validation.constraints.NotBlank;

public class CarCreateDto {

    @NotBlank
    private Long modelId;

    @NotBlank
    private Long typeId;

    @NotBlank
    private Long companyId;

    @NotBlank
    private Integer rentalDayPrice;

    public CarCreateDto() {
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getRentalDayPrice() {
        return rentalDayPrice;
    }

    public void setRentalDayPrice(Integer rentalDayPrice) {
        this.rentalDayPrice = rentalDayPrice;
    }
}
