package com.example.SK_Project2.RentalCarService.service;

import com.example.SK_Project2.RentalCarService.dto.model.ModelCreateDto;
import com.example.SK_Project2.RentalCarService.dto.model.ModelDto;

import java.util.List;

public interface ModelService {

    List<ModelDto> findAll();

    ModelDto findById(Long id);

    ModelDto add(ModelCreateDto modelCreateDto);

    Boolean delete(Long id);

    ModelDto update(ModelDto modelDto);
}
