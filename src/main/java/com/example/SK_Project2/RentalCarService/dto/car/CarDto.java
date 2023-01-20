package com.example.SK_Project2.RentalCarService.dto.car;

import java.util.Date;

public class CarDto {

    private Long id;
    private String modelName;
    private String typeName;
    private String companyName;
    private Integer rentalDayPrice;
//    private boolean reserved;
//    private Date startDate;
//    private Date endDate;

    public CarDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getRentalDayPrice() {
        return rentalDayPrice;
    }

    public void setRentalDayPrice(Integer rentalDayPrice) {
        this.rentalDayPrice = rentalDayPrice;
    }
}

