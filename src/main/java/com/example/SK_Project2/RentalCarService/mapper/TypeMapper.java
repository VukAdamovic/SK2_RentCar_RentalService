package com.example.SK_Project2.RentalCarService.mapper;

import com.example.SK_Project2.RentalCarService.domain.Type;
import com.example.SK_Project2.RentalCarService.dto.type.TypeCreateDto;
import com.example.SK_Project2.RentalCarService.dto.type.TypeDto;
import org.springframework.stereotype.Component;

@Component
public class TypeMapper {

    public TypeDto typeToTypeDto (Type type) {
        TypeDto typeDto = new TypeDto();

        typeDto.setId(type.getId());
        typeDto.setName(type.getName());

        return typeDto;
    }


    public Type typeCreateDtoToType (TypeCreateDto typeCreateDto) {
        Type type = new Type();

        type.setName(typeCreateDto.getName());

        return type;
    }
}
