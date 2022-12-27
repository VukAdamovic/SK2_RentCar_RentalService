package com.example.SK_Project2.RentalCarService.dto.notifications;

public class ReminderDto {
    private String email;
    private String car;

    public ReminderDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }
}
