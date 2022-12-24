package com.example.SK_Project2.RentalCarService.exception;

import org.springframework.http.HttpStatus;

public class AccessForbidden extends CustomException{

    public AccessForbidden(String message) {
        super(message, ErrorCode.ACCESS_FORBIDDEN,HttpStatus.FORBIDDEN);
    }
}
