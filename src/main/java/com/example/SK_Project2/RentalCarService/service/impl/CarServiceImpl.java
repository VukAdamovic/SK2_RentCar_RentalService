package com.example.SK_Project2.RentalCarService.service.impl;

import com.example.SK_Project2.RentalCarService.domain.*;
import com.example.SK_Project2.RentalCarService.dto.car.CarCreateDto;
import com.example.SK_Project2.RentalCarService.dto.car.CarDto;
import com.example.SK_Project2.RentalCarService.dto.car.CarFilterDto;
import com.example.SK_Project2.RentalCarService.dto.company.CompanyDto;
import com.example.SK_Project2.RentalCarService.exception.NotFoundException;
import com.example.SK_Project2.RentalCarService.exception.OperationNotAllowed;
import com.example.SK_Project2.RentalCarService.mapper.*;
import com.example.SK_Project2.RentalCarService.repository.*;
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
    private ReservationRepository reservationRepository;
    private ReservationMapper reservationMapper;
    private ModelMapper modelMapper;
    private TypeMapper typeMapper;
    private CompanyMapper companyMapper;
    private CarMapper carMapper;

    public CarServiceImpl(ModelRepository modelRepository, TypeRepository typeRepository, CompanyRepository companyRepository, CarRepository carRepository, ReservationRepository reservationRepository,
                          ReservationMapper reservationMapper, ModelMapper modelMapper, TypeMapper typeMapper, CompanyMapper companyMapper, CarMapper carMapper) {
        this.modelRepository = modelRepository;
        this.typeRepository = typeRepository;
        this.companyRepository = companyRepository;
        this.carRepository = carRepository;
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.modelMapper = modelMapper;
        this.typeMapper = typeMapper;
        this.companyMapper = companyMapper;
        this.carMapper = carMapper;
    }

    @Override
    public CarDto add(CarCreateDto carCreateDto) {
        Long companyId = carCreateDto.getCompanyId();
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException(String.format("Company with id: %d does not exists.", companyId)));
        CompanyDto companyDto = companyMapper.companyToCompanyDto(company);

        if(company.getNumOfCars() >= companyDto.getCarList().size() + 1) {
            Car car = carMapper.carCreateDtoToCar(carCreateDto);

            carRepository.save(car);

            return carMapper.carToCarDto(car);
        }

        throw new OperationNotAllowed(String.format("Company contains max number of cars"));
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

        car.setRentalDayPrice(carDto.getRentalDayPrice());

        carRepository.save(car);

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

    @Override
    public List<CarDto> findCarFilter(CarFilterDto carFilterDto) {
        List<CarDto> availableCars = new ArrayList<>();

        Company company = companyRepository.findCompanyById(carFilterDto.getCompany_id())
               .orElseThrow(() -> new NotFoundException(String.format("Company with id: %d does not exists.", carFilterDto.getCompany_id())));

        List<Car> allCars = carRepository.findAll();

        for(Car car : allCars){
            if(!car.isReserved() && car.getCompany().equals(company) && car.getCompany().getCity().equals(carFilterDto.getCity())){
                availableCars.add(carMapper.carToCarDto(car));
                continue;
            }

            if(car.isReserved() && car.getCompany().equals(company) && car.getCompany().getCity().equals(carFilterDto.getCity())){
                List<Reservation> allCarReservation = new ArrayList<>();

                reservationRepository.findAll().forEach(reservation -> {
                    if(reservation.getCar().equals(car))
                        allCarReservation.add(reservation);
                });

                boolean check = true;
               for(Reservation reservation : allCarReservation){
                   if(!((carFilterDto.getStartDate().before(reservation.getStartDate()) && carFilterDto.getEndDate().before(reservation.getStartDate()))
                           || (carFilterDto.getStartDate().after(reservation.getEndDate()) && carFilterDto.getEndDate().after(reservation.getEndDate())))){
                       check = false;
                   }
               }

               if(check){
                   availableCars.add(carMapper.carToCarDto(car));
               }
            }
        }

        return availableCars;
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
