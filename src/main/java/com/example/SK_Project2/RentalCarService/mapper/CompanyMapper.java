package com.example.SK_Project2.RentalCarService.mapper;


import com.example.SK_Project2.RentalCarService.domain.Company;
import com.example.SK_Project2.RentalCarService.dto.car.CarDto;
import com.example.SK_Project2.RentalCarService.dto.company.CompanyCreateDto;
import com.example.SK_Project2.RentalCarService.dto.company.CompanyDto;
import com.example.SK_Project2.RentalCarService.repository.CarRepository;
import com.example.SK_Project2.RentalCarService.repository.CompanyRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompanyMapper {

    private CompanyRepository companyRepository;
    private CarRepository carRepository;
    private CarMapper carMapper;

    public CompanyMapper(CompanyRepository companyRepository, CarRepository carRepository, CarMapper carMapper) {
        this.companyRepository = companyRepository;
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    public CompanyDto companyToCompanyDto (Company company) {
        CompanyDto companyDto = new CompanyDto();

        companyDto.setId(company.getId());
        companyDto.setName(company.getName());
        companyDto.setDescription(company.getDescription());
        companyDto.setNumOfCars(company.getNumOfCars());
        companyDto.setCity(company.getCity());

        //set carList
        List<CarDto> cars = new ArrayList<>();
        carRepository.findAll().
                forEach(car -> {
                    if(car.getCompany().getName().equals(company.getName())){
                        cars.add(carMapper.carToCarDto(car));
                    }
                });
        companyDto.setCarList(cars);

        return companyDto;
    }

    public Company companyCreateDtoToCompany (CompanyCreateDto companyCreateDto) {
        Company company = new Company();

        company.setName(companyCreateDto.getName());
        company.setDescription(companyCreateDto.getDescription());
        company.setNumOfCars(companyCreateDto.getNumOfCars());
        company.setCity(companyCreateDto.getCity());

        return company;
    }


}
