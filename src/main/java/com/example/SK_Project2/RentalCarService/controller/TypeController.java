package com.example.SK_Project2.RentalCarService.controller;

import com.example.SK_Project2.RentalCarService.dto.type.TypeCreateDto;
import com.example.SK_Project2.RentalCarService.dto.type.TypeDto;
import com.example.SK_Project2.RentalCarService.security.CheckSecurity;
import com.example.SK_Project2.RentalCarService.service.TypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rental/type")
public class TypeController {

    private TypeService typeService;

    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    @GetMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<List<TypeDto>> getAllTypes(@RequestHeader("authorization") String authorization){
        return new ResponseEntity<>(typeService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<TypeDto> getTypeById(@RequestHeader("authorization") String authorization,@PathVariable("id") Long id){
        return new ResponseEntity<>(typeService.findById(id),HttpStatus.OK);
    }

    //---------------------

    @PostMapping("/registration")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<TypeDto> registerType(@RequestHeader("authorization") String authorization,@RequestBody TypeCreateDto typeCreateDto){
        return new ResponseEntity<>(typeService.add(typeCreateDto),HttpStatus.CREATED);
    }

    //---------------------

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<Boolean> deleteType(@RequestHeader("authorization") String authorization,@PathVariable("id") Long id){
        return new ResponseEntity<>(typeService.delete(id),HttpStatus.OK);
    }

    //---------------------

    @PutMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<TypeDto> updateType(@RequestHeader("authorization") String authorization,@RequestBody TypeDto typeDto){
        return new ResponseEntity<>(typeService.update(typeDto),HttpStatus.OK);
    }
}
