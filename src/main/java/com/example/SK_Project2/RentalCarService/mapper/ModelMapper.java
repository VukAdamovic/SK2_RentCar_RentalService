package com.example.SK_Project2.RentalCarService.mapper;

import com.example.SK_Project2.RentalCarService.domain.Model;
import com.example.SK_Project2.RentalCarService.dto.model.ModelCreateDto;
import com.example.SK_Project2.RentalCarService.dto.model.ModelDto;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    public ModelDto modelToModelDto(Model model){
        ModelDto modelDto = new ModelDto();

        modelDto.setId(model.getId());
        modelDto.setName(model.getName());

        return modelDto;
    }

    public Model modelCreateDtoToModel(ModelCreateDto modelCreateDto){
        Model model = new Model();

        model.setName(modelCreateDto.getName());

        return model;
    }
}
