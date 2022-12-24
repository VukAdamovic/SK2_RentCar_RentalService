package com.example.SK_Project2.RentalCarService.controller;

import com.example.SK_Project2.RentalCarService.dto.model.ModelCreateDto;
import com.example.SK_Project2.RentalCarService.dto.model.ModelDto;
import com.example.SK_Project2.RentalCarService.security.CheckSecurity;
import com.example.SK_Project2.RentalCarService.service.ModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rental/model")
public class ModelController {

    private ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<List<ModelDto>> getAllModels(@RequestHeader("authorization") String authorization){
        return new ResponseEntity<>(modelService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<ModelDto> getModelById(@RequestHeader("authorization") String authorization,@PathVariable("id") Long id){
        return new ResponseEntity<>(modelService.findById(id),HttpStatus.OK);
    }

    //---------------------

    @PostMapping("/registration")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<ModelDto> registerModel(@RequestHeader("authorization") String authorization, @RequestBody ModelCreateDto modelCreateDto){
        return new ResponseEntity<>(modelService.add(modelCreateDto),HttpStatus.CREATED);
    }

    //---------------------

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<Boolean> deleteModel(@RequestHeader("authorization") String authorization,@PathVariable("id") Long id){
        return new ResponseEntity<>(modelService.delete(id),HttpStatus.OK);
    }

    //---------------------

    @PutMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<ModelDto> updateModel(@RequestHeader("authorization") String authorization,@RequestBody ModelDto modelDto){
        return new ResponseEntity<>(modelService.update(modelDto),HttpStatus.OK);
    }
}
