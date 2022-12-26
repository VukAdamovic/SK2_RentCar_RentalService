package com.example.SK_Project2.RentalCarService.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Model model;

    @ManyToOne
    private Type type;

    @ManyToOne
    private Company company;

    private Integer rentalDayPrice;

    private boolean reserved;


    public Car() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Integer getRentalDayPrice() {
        return rentalDayPrice;
    }

    public void setRentalDayPrice(Integer rentalDayPrice) {
        this.rentalDayPrice = rentalDayPrice;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

}
