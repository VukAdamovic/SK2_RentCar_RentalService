package com.example.SK_Project2.RentalCarService.service;

import com.example.SK_Project2.RentalCarService.dto.type.TypeCreateDto;
import com.example.SK_Project2.RentalCarService.dto.type.TypeDto;

import java.util.List;

public interface TypeService {

    List<TypeDto> findAll();

    TypeDto findById(Long id);

    TypeDto add(TypeCreateDto typeCreateDto);

    Boolean delete(Long id);

    TypeDto update(TypeDto typeDto);
}
