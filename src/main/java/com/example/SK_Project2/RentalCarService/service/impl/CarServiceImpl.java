package com.example.SK_Project2.RentalCarService.service.impl;

import com.example.SK_Project2.RentalCarService.domain.Car;
import com.example.SK_Project2.RentalCarService.domain.Company;
import com.example.SK_Project2.RentalCarService.domain.Model;
import com.example.SK_Project2.RentalCarService.domain.Type;
import com.example.SK_Project2.RentalCarService.dto.car.CarCreateDto;
import com.example.SK_Project2.RentalCarService.dto.car.CarDto;
import com.example.SK_Project2.RentalCarService.exception.NotFoundException;
import com.example.SK_Project2.RentalCarService.mapper.CarMapper;
import com.example.SK_Project2.RentalCarService.mapper.CompanyMapper;
import com.example.SK_Project2.RentalCarService.mapper.ModelMapper;
import com.example.SK_Project2.RentalCarService.mapper.TypeMapper;
import com.example.SK_Project2.RentalCarService.repository.CarRepository;
import com.example.SK_Project2.RentalCarService.repository.CompanyRepository;
import com.example.SK_Project2.RentalCarService.repository.ModelRepository;
import com.example.SK_Project2.RentalCarService.repository.TypeRepository;
import com.example.SK_Project2.RentalCarService.service.CarService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    private ModelRepository modelRepository;
    private TypeRepository typeRepository;
    private CompanyRepository companyRepository;
    private CarRepository carRepository;
    private ModelMapper modelMapper;
    private TypeMapper typeMapper;
    private CompanyMapper companyMapper;
    private CarMapper carMapper;

    public CarServiceImpl(ModelRepository modelRepository, TypeRepository typeRepository, CompanyRepository companyRepository, CarRepository carRepository,
                          ModelMapper modelMapper, TypeMapper typeMapper, CompanyMapper companyMapper, CarMapper carMapper) {
        this.modelRepository = modelRepository;
        this.typeRepository = typeRepository;
        this.companyRepository = companyRepository;
        this.carRepository = carRepository;
        this.modelMapper = modelMapper;
        this.typeMapper = typeMapper;
        this.companyMapper = companyMapper;
        this.carMapper = carMapper;
    }

    @Override
    public CarDto add(CarCreateDto carCreateDto) {
        Car car = carMapper.carCreateDtoToCar(carCreateDto);

        carRepository.save(car);

        return carMapper.carToCarDto(car);
    }

    @Override
    public Boolean delete(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Car with id: %d does not exists.", id)));

        carRepository.delete(car);
        return true;
    }

    @Override
    public CarDto update(CarDto carDto) {
        Car car = carRepository.findById(carDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Car with id: %d does not exists.", carDto.getId())));

        car.setId(carDto.getId());

        //setModel
        Model model = modelRepository.findModelByName(carDto.getModelName())
                .orElseThrow(() -> new NotFoundException(String.format("Model with id: %d does not exists.", carDto.getModelName())));
        car.setModel(model);

        //setType
        Type type = typeRepository.findTypeByName(carDto.getTypeName())
                .orElseThrow(() -> new NotFoundException(String.format("Type with id: %d does not exists.",carDto.getTypeName())));
        car.setType(type);

        //setCompany
        Company company = companyRepository.findCompanyByName(carDto.getCompanyName())
                .orElseThrow(() -> new NotFoundException(String.format("Company with id: %d does not exists.", carDto.getCompanyName())));

        car.setCompany(company);

        car.setRentalDayPrice(car.getRentalDayPrice());
        car.setReserved(car.isReserved());
        car.setStartDate(car.getStartDate());
        car.setEndDate(car.getEndDate());

        return carMapper.carToCarDto(car);

    }


    @Override
    public List<CarDto> findAll() {
        List<CarDto> cars = new ArrayList<>();
        carRepository.findAll()
                .forEach(car -> {
                    cars.add(carMapper.carToCarDto(car));
                });

        return cars;
    }

    @Override
    public CarDto findById(Long id) {
        return carRepository.findById(id)
                .map(carMapper::carToCarDto)
                .orElseThrow(() -> new NotFoundException(String.format("Car with id: %d does not exists.", id)));
    }


    //proveri metodu lose objasnjeno u specifikaciji
    @Override
    public List<CarDto> findByCustomParams(String city, String companyName, Date startDate, Date endDate) {
        List<CarDto> cars = new ArrayList<>();

        carRepository.findAll()
                .forEach(car -> {
                    if(!car.isReserved() && (car.getCompany().getCity().equals(city)) && car.getCompany().getName().equals(companyName)
                            && car.getEndDate().before(startDate) && car.getEndDate().before(endDate)){
                        cars.add(carMapper.carToCarDto(car));
                    }
                });
        return cars;
    }

    //-----------------------------------------------------------//

    @Override
    public List<CarDto> sortByDayPriceASC() {
        List<CarDto> cars = new ArrayList<>();

        carRepository.findAll()
                .forEach(car -> {
                    if(!car.isReserved()){
                        cars.add(carMapper.carToCarDto(car));
                    }
                });
        cars.sort(Comparator.comparing(CarDto::getRentalDayPrice));

        return cars;
    }

    @Override
    public List<CarDto> sortByDayPriceDESC() {
        List<CarDto> cars = new ArrayList<>();

        carRepository.findAll()
                .forEach(car -> {
                    if(!car.isReserved()){
                        cars.add(carMapper.carToCarDto(car));
                    }
                });

        cars.sort(Comparator.comparing(CarDto::getRentalDayPrice).reversed());

        return cars;
    }
}
