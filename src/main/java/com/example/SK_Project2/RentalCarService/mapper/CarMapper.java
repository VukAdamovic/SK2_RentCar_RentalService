package com.example.SK_Project2.RentalCarService.mapper;

import com.example.SK_Project2.RentalCarService.domain.Car;
import com.example.SK_Project2.RentalCarService.domain.Company;
import com.example.SK_Project2.RentalCarService.domain.Model;
import com.example.SK_Project2.RentalCarService.domain.Type;
import com.example.SK_Project2.RentalCarService.dto.car.CarCreateDto;
import com.example.SK_Project2.RentalCarService.dto.car.CarDto;
import com.example.SK_Project2.RentalCarService.exception.NotFoundException;
import com.example.SK_Project2.RentalCarService.repository.CompanyRepository;
import com.example.SK_Project2.RentalCarService.repository.ModelRepository;
import com.example.SK_Project2.RentalCarService.repository.TypeRepository;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {
    private TypeRepository typeRepository;
    private ModelRepository modelRepository;
    private CompanyRepository companyRepository;

    public CarMapper(TypeRepository typeRepository, ModelRepository modelRepository, CompanyRepository companyRepository) {
        this.typeRepository = typeRepository;
        this.modelRepository = modelRepository;
        this.companyRepository = companyRepository;
    }

    public CarDto carToCarDto(Car car){
        CarDto carDto = new CarDto();

        carDto.setId(car.getId());
        carDto.setModelName(car.getModel().getName());
        carDto.setTypeName(car.getType().getName());
        carDto.setCompanyName(car.getCompany().getName());
        carDto.setRentalDayPrice(car.getRentalDayPrice());
//        carDto.setReserved(car.isReserved());

        return carDto;
    }

    public Car carCreateDtoToCar(CarCreateDto carCreateDto){
        Car car = new Car();

        //setModel
        Model model = modelRepository.findModelById(carCreateDto.getModelId())
                .orElseThrow(() -> new NotFoundException(String.format("Model with id: %d does not exists.", carCreateDto.getModelId())));
        car.setModel(model);

        //setType
        Type type = typeRepository.findTypeById(carCreateDto.getTypeId())
                .orElseThrow(() -> new NotFoundException(String.format("Type with id: %d does not exists.", carCreateDto.getTypeId())));
        car.setType(type);

        //setCompany
        Company company = companyRepository.findCompanyById(carCreateDto.getCompanyId())
                .orElseThrow(() -> new NotFoundException(String.format("Company with id: %d does not exists.", carCreateDto.getCompanyId())));

        car.setCompany(company);

        car.setRentalDayPrice(carCreateDto.getRentalDayPrice());
        car.setReserved(false);

        return car;
    }
}
