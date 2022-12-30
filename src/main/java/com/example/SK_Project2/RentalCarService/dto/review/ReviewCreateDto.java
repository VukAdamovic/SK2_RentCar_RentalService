package com.example.SK_Project2.RentalCarService.dto.review;

import javax.validation.constraints.NotBlank;

public class ReviewCreateDto {

    @NotBlank
    private Long companyId;
    @NotBlank
    private Integer rate;
    @NotBlank
    private String desc;


    public ReviewCreateDto() {
    }


    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

