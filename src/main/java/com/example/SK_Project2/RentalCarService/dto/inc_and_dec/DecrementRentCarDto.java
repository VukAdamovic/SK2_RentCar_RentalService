package com.example.SK_Project2.RentalCarService.dto.inc_and_dec;

public class DecrementRentCarDto {
    private Long id;
    private Integer days;

    public DecrementRentCarDto() {
    }

    public DecrementRentCarDto(Long id, Integer days) {
        this.id = id;
        this.days = days;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}
