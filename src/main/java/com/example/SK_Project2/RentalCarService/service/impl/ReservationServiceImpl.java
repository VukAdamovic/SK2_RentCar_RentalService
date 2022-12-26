package com.example.SK_Project2.RentalCarService.service.impl;

import com.example.SK_Project2.RentalCarService.domain.Car;
import com.example.SK_Project2.RentalCarService.domain.Reservation;
import com.example.SK_Project2.RentalCarService.dto.inc_and_dec.DecrementRentCarDto;
import com.example.SK_Project2.RentalCarService.dto.inc_and_dec.IncrementRentCarDto;
import com.example.SK_Project2.RentalCarService.dto.notifications.CanceledReservationDto;
import com.example.SK_Project2.RentalCarService.dto.notifications.SuccessfulReservationDto;
import com.example.SK_Project2.RentalCarService.dto.reservation.ReservationCreateDto;
import com.example.SK_Project2.RentalCarService.dto.reservation.ReservationDto;
import com.example.SK_Project2.RentalCarService.exception.NotFoundException;
import com.example.SK_Project2.RentalCarService.exception.OperationNotAllowed;
import com.example.SK_Project2.RentalCarService.mapper.ReservationMapper;
import com.example.SK_Project2.RentalCarService.messageHelper.MessageHelper;
import com.example.SK_Project2.RentalCarService.repository.CarRepository;
import com.example.SK_Project2.RentalCarService.repository.ReservationRepository;
import com.example.SK_Project2.RentalCarService.security.service.TokenService;
import com.example.SK_Project2.RentalCarService.service.ReservationService;
import com.example.SK_Project2.RentalCarService.userConfiguration.dto.DiscountDto;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {
    private TokenService tokenService;
    private ReservationRepository reservationRepository;
    private ReservationMapper reservationMapper;
    private CarRepository carRepository;
    private JmsTemplate jmsTemplate;
    private MessageHelper messageHelper;
    private RestTemplate userServiceRestTemplate; //za sinhronu
    private String incrementRentCarDestination;
    private String decrementRentCarDestination;
    private String successfulReservationDestination;
    private String canceledReservationDestination;

    // jos jedna notifikacija dest


    public ReservationServiceImpl(TokenService tokenService, ReservationRepository reservationRepository, ReservationMapper reservationMapper, CarRepository carRepository, JmsTemplate jmsTemplate, MessageHelper messageHelper, RestTemplate userServiceRestTemplate,
                                  @Value("${destination.incrementRentCar}")String incrementRentCarDestination, @Value("${destination.decrementRentCar}")String decrementRentCarDestination,
                                  @Value("${destination.successfulReservation}") String successfulReservationDestination,@Value("${destination.canceledReservation}") String canceledReservationDestination) {
        this.tokenService = tokenService;
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.carRepository = carRepository;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.userServiceRestTemplate = userServiceRestTemplate;
        this.incrementRentCarDestination = incrementRentCarDestination;
        this.decrementRentCarDestination = decrementRentCarDestination;
        this.successfulReservationDestination = successfulReservationDestination;
        this.canceledReservationDestination = canceledReservationDestination;
    }

    @Override
    public ReservationDto addReservation(String authorization, ReservationCreateDto reservationCreateDto) {
        Claims claims = tokenService.parseToken(authorization.split(" ")[1]);
        Long clientId = claims.get("id",Long.class);
        String emailClient = claims.get("email",String.class);


        Date now = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        if(reservationCreateDto.getStartDate().before(now) || reservationCreateDto.getStartDate().after(reservationCreateDto.getEndDate())){
           throw new OperationNotAllowed(String.format("Check dates!"));
        }

        //--------------------------------------------//

        Reservation reservation = reservationMapper.reservationCreateDtoToReservation(reservationCreateDto);
        reservation.setUserId(clientId);

        Car car = carRepository.findById(reservation.getCarId())
                .orElseThrow(() -> new NotFoundException(String.format("Car with id: %d does not exists.", reservation.getCarId())));

        if(!car.isReserved() || (car.getEndDate().before(reservation.getStartDate()) && now.after(car.getEndDate()))){
            car.setReserved(true);
            car.setStartDate(reservation.getStartDate());
            car.setEndDate(reservation.getEndDate());

            carRepository.save(car);

            //--------------------------------------------//
            Duration diff = Duration.between(reservation.getStartDate().toInstant(), reservation.getEndDate().toInstant());
            Integer days = Math.toIntExact(diff.toDays()); // reservation.getEndDate().getDay() - reservation.getStartDate().getDay()

            IncrementRentCarDto incrementRentCarDto = new IncrementRentCarDto();
            incrementRentCarDto.setId(reservation.getUserId());
            incrementRentCarDto.setDays(days);
            jmsTemplate.convertAndSend(incrementRentCarDestination,messageHelper.createTextMessage(incrementRentCarDto));

            //-------------------------------------------//

            //sinhona komunikacija, ali sta znaci retry ?
            ResponseEntity<DiscountDto> discountDtoResponseEntity = userServiceRestTemplate.exchange("/users/client/" + clientId + "/discount",
                    HttpMethod.GET,null, DiscountDto.class);


            DiscountDto discountDto = discountDtoResponseEntity.getBody();

            Double discount = Double.valueOf((days * car.getRentalDayPrice())) * Double.valueOf(discountDto.getDiscount() / 100);
            Double price = Double.valueOf((days * car.getRentalDayPrice())) - discount;


            SuccessfulReservationDto successfulReservationDto = new SuccessfulReservationDto();

            successfulReservationDto.setEmail(emailClient);
            successfulReservationDto.setCar(car.getModel() + " " + car.getType());
            successfulReservationDto.setStartDate(reservation.getStartDate());
            successfulReservationDto.setEndDate(reservation.getEndDate());
            successfulReservationDto.setPrice(String.valueOf(price));

            jmsTemplate.convertAndSend(successfulReservationDestination,messageHelper.createTextMessage(successfulReservationDto));

            //-------------------------------------------//

            reservationRepository.save(reservation);
            return reservationMapper.resevationToReservationDto(reservation);
        }

        throw  new OperationNotAllowed(String.format("Reservation was not successful!"));

    }

    @Override
    public Boolean canceleReservation(String authorization, Long id) {
        Claims claims = tokenService.parseToken(authorization.split(" ")[1]);
        String roleName = claims.get("role",String.class);
        String email = claims.get("email",String.class);

        if (roleName.equals("ROLE_ADMIN")){
            new OperationNotAllowed(String.format("Role admin can not canc"));
        }

        Reservation reservation = reservationRepository.findById(id).
                orElseThrow(()->new NotFoundException(String.format("Reservation with id: %d does not exists.",id)));


        Long clientId = reservation.getUserId();
        Car car = carRepository.findById(reservation.getCarId())
                .orElseThrow(() -> new NotFoundException(String.format("Car with id: %d does not exists.", reservation.getCarId())));


        //--------------------------------------------//
        Duration diff = Duration.between(reservation.getStartDate().toInstant(), reservation.getEndDate().toInstant());
        Integer days = Math.toIntExact(diff.toDays()); // reservation.getEndDate().getDay() - reservation.getStartDate().getDay()

        DecrementRentCarDto decrementRentCarDto = new DecrementRentCarDto();
        decrementRentCarDto.setId(clientId);
        decrementRentCarDto.setDays(days);

        jmsTemplate.convertAndSend(decrementRentCarDestination,messageHelper.createTextMessage(decrementRentCarDto));

        //-------------------------------------------//

        CanceledReservationDto canceledReservationDto = new CanceledReservationDto();

        canceledReservationDto.setEmail(email);
        canceledReservationDto.setCar(car.getModel() + " " + car.getType());

        jmsTemplate.convertAndSend(canceledReservationDestination,messageHelper.createTextMessage(canceledReservationDto));

        //-------------------------------------------//


        //Refresujem vrednosti
        reservationRepository.delete(reservation);

        car.setReserved(false);
        car.setStartDate(null);
        car.setEndDate(null);

        carRepository.save(car);

        return true;
    }
}
