package com.example.SK_Project2.RentalCarService.userConfiguration.dto;

public class DiscountDto {
    private Integer discount;

    public DiscountDto() {
    }

    public DiscountDto(Integer discount) {
        this.discount = discount;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}
