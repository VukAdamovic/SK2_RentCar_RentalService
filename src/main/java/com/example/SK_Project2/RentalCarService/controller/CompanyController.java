package com.example.SK_Project2.RentalCarService.controller;

import com.example.SK_Project2.RentalCarService.dto.company.CompanyCreateDto;
import com.example.SK_Project2.RentalCarService.dto.company.CompanyDto;
import com.example.SK_Project2.RentalCarService.security.CheckSecurity;
import com.example.SK_Project2.RentalCarService.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rental/company")
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<List<CompanyDto>> getAllCompanies(@RequestHeader("authorization") String authorization){
        return new ResponseEntity<>(companyService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<CompanyDto> getCompanyById(@RequestHeader("authorization") String authorization, @PathVariable("id") Long id){
        return new ResponseEntity<>(companyService.findById(id),HttpStatus.OK);
    }

    @GetMapping("/sortCompanies")
    public ResponseEntity<List<CompanyDto>> sortCompaniesByRate(){
        return new ResponseEntity<>(companyService.sortCompaniesByReview(),HttpStatus.OK);
    }

    //---------------------

    @PostMapping("/registration")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<CompanyDto> registerCompany(@RequestHeader("authorization") String authorization, @RequestBody CompanyCreateDto companyCreateDto){
        return new ResponseEntity<>(companyService.add(companyCreateDto),HttpStatus.CREATED);
    }

    //---------------------

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<Boolean> deleteCompany(@RequestHeader("authorization") String authorization,@PathVariable("id") Long id){
        return new ResponseEntity<>(companyService.delete(id),HttpStatus.OK);
    }

    //---------------------

    @PutMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<CompanyDto> updateCompany(@RequestHeader("authorization") String authorization,@RequestBody CompanyDto companyDto){
        return new ResponseEntity<>(companyService.update(companyDto),HttpStatus.OK);
    }
}
