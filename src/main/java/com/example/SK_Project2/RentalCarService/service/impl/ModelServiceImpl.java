package com.example.SK_Project2.RentalCarService.service.impl;

import com.example.SK_Project2.RentalCarService.domain.Model;
import com.example.SK_Project2.RentalCarService.dto.model.ModelCreateDto;
import com.example.SK_Project2.RentalCarService.dto.model.ModelDto;
import com.example.SK_Project2.RentalCarService.exception.NotFoundException;
import com.example.SK_Project2.RentalCarService.mapper.ModelMapper;
import com.example.SK_Project2.RentalCarService.repository.ModelRepository;
import com.example.SK_Project2.RentalCarService.service.ModelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ModelServiceImpl implements ModelService {
    private ModelRepository modelRepository;
    private ModelMapper modelMapper;

    public ModelServiceImpl(ModelRepository modelRepository, ModelMapper modelMapper) {
        this.modelRepository = modelRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ModelDto> findAll() {
        List<ModelDto> models = new ArrayList<>();
        modelRepository.findAll()
                .forEach(model -> {
                    models.add(modelMapper.modelToModelDto(model));
                });
        return models;
    }

    @Override
    public ModelDto findById(Long id) {
        return modelRepository.findById(id)
                .map(modelMapper::modelToModelDto)
                .orElseThrow(() -> new NotFoundException(String.format("Model with id: %d does not exists.", id)));
    }

    @Override
    public ModelDto add(ModelCreateDto modelCreateDto) {
        Model model = modelMapper.modelCreateDtoToModel(modelCreateDto);
        modelRepository.save(model);

        return modelMapper.modelToModelDto(model);
    }

    @Override
    public Boolean delete(Long id) {
        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Model with id: %d does not exists.", id)));

        modelRepository.delete(model);
        return true;
    }

    @Override
    public ModelDto update(ModelDto modelDto) {
        Model model = modelRepository.findById(modelDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Model with id: %d does not exists.", modelDto.getId())));

        model.setId(modelDto.getId());
        model.setName(modelDto.getName());

        modelRepository.save(model);

        return modelMapper.modelToModelDto(model);
    }

}
