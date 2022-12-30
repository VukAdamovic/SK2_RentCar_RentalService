package com.example.SK_Project2.RentalCarService.dto.review;

public class ReviewFilterDto {
    private String city;
    private Long company_id;

    public ReviewFilterDto() {
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }
}
