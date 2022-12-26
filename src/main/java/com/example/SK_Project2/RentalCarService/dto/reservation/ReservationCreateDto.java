package com.example.SK_Project2.RentalCarService.dto.reservation;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public class ReservationCreateDto {

    @NotBlank
    private Long carId;
    @NotBlank
    private Date startDate;
    @NotBlank
    private Date endDate;

    public ReservationCreateDto() {
    }


    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
