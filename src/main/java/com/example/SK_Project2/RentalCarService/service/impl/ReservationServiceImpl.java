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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


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
        Long clientId = claims.get("id",Integer.class).longValue();
        String emailClient = claims.get("email",String.class);


        Date now = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        if(reservationCreateDto.getStartDate().before(now) || reservationCreateDto.getStartDate().after(reservationCreateDto.getEndDate())){
           throw new OperationNotAllowed(String.format("Check dates!"));
        }

        //--------------------------------------------//

        Reservation newReservation = reservationMapper.reservationCreateDtoToReservation(reservationCreateDto);
        newReservation.setUserId(clientId);
        newReservation.setEmail(emailClient);

        Car car = newReservation.getCar();


        if(!car.isReserved()){
            car.setReserved(true);
            carRepository.save(car);

            //--------------------------------------------//
            Duration diff = Duration.between(newReservation.getStartDate().toInstant(), newReservation.getEndDate().toInstant());
            Integer days = Math.toIntExact(diff.toDays())  + 1;  // + 1 mozda

            IncrementRentCarDto incrementRentCarDto = new IncrementRentCarDto();
            incrementRentCarDto.setId(newReservation.getUserId());
            incrementRentCarDto.setDays(days);
            jmsTemplate.convertAndSend(incrementRentCarDestination,messageHelper.createTextMessage(incrementRentCarDto));

            //-------------------------------------------//

            //sinhona komunikacija, ali sta znaci retry ?
            ResponseEntity<DiscountDto> discountDtoResponseEntity = userServiceRestTemplate.exchange("/users/client/" + clientId + "/discount",
                    HttpMethod.GET,null, DiscountDto.class);

            DiscountDto discountDto = discountDtoResponseEntity.getBody();
            System.out.println("Pre " + discountDto.getDiscount());

            Double discount = Double.valueOf((days * car.getRentalDayPrice())) * Double.valueOf(discountDto.getDiscount() * 0.01);
            Double price = Double.valueOf((days * car.getRentalDayPrice())) - discount;
            System.out.println("Popust:" + discount);
            System.out.println("Cena sa popustom" + price);

            newReservation.setTotalPrice(price.intValue()); //setuj cenu


            //-----------------------------------------------------------------//
            //asinhrona komunikacija

            SuccessfulReservationDto successfulReservationDto = new SuccessfulReservationDto();

            successfulReservationDto.setEmail(emailClient);
            successfulReservationDto.setCar(car.getModel().getName() + " " + car.getType().getName());
            successfulReservationDto.setStartDate(newReservation.getStartDate());
            successfulReservationDto.setEndDate(newReservation.getEndDate());
            successfulReservationDto.setPrice(String.valueOf(price));

            jmsTemplate.convertAndSend(successfulReservationDestination,messageHelper.createTextMessage(successfulReservationDto));

            //-------------------------------------------//

            reservationRepository.save(newReservation);
            return reservationMapper.resevationToReservationDto(newReservation);
        }

        //--------------------------------------------//
        //AKO VEC postoji rezervacija za taj auto


        List<Reservation> reservations = new ArrayList<>();
        reservationRepository.findAll().forEach(oldReservation -> {
            if (oldReservation.getCar().equals(car)) {
                    reservations.add(oldReservation);
            }
        });


        boolean check = true;
        for(Reservation oldReservation : reservations){
            if(!((newReservation.getStartDate().before(oldReservation.getStartDate()) && newReservation.getEndDate().before(oldReservation.getStartDate()))
                    || (newReservation.getStartDate().after(oldReservation.getEndDate()) && newReservation.getEndDate().after(oldReservation.getEndDate())))){
                check = false;   //ako bilo koja stara rezervacija preklapa novu rezervaciju ne mozes da rezervises
            }
        }

        System.out.println("LISTA: " + reservations);
        //ako moze da se uklopi nova rezervacija u termine sa starim
        if(check){
            Duration diff = Duration.between(newReservation.getStartDate().toInstant(), newReservation.getEndDate().toInstant());
            Integer days = Math.toIntExact(diff.toDays())  + 1;  // + 1 mozda

            IncrementRentCarDto incrementRentCarDto = new IncrementRentCarDto();
            incrementRentCarDto.setId(newReservation.getUserId());
            incrementRentCarDto.setDays(days);
            jmsTemplate.convertAndSend(incrementRentCarDestination,messageHelper.createTextMessage(incrementRentCarDto));

            //-------------------------------------------//

            //sinhona komunikacija, ali sta znaci retry ?
            ResponseEntity<DiscountDto> discountDtoResponseEntity = userServiceRestTemplate.exchange("/users/client/" + clientId + "/discount",
                    HttpMethod.GET,null, DiscountDto.class);

            DiscountDto discountDto = discountDtoResponseEntity.getBody();
            System.out.println("Pre " + discountDto.getDiscount());

            Double discount = Double.valueOf((days * car.getRentalDayPrice())) * Double.valueOf(discountDto.getDiscount() * 0.01);
            Double price = Double.valueOf((days * car.getRentalDayPrice())) - discount;
            System.out.println("Popust:" + discount);
            System.out.println("Cena sa popustom" + price);

            newReservation.setTotalPrice(price.intValue()); //setuj cenu


            //-----------------------------------------------------------------//
            //asinhrona komunikacija

            SuccessfulReservationDto successfulReservationDto = new SuccessfulReservationDto();

            successfulReservationDto.setEmail(emailClient);
            successfulReservationDto.setCar(car.getModel().getName() + " " + car.getType().getName());
            successfulReservationDto.setStartDate(newReservation.getStartDate());
            successfulReservationDto.setEndDate(newReservation.getEndDate());
            successfulReservationDto.setPrice(String.valueOf(price));

            jmsTemplate.convertAndSend(successfulReservationDestination,messageHelper.createTextMessage(successfulReservationDto));

            //-------------------------------------------//

            reservationRepository.save(newReservation);
            return reservationMapper.resevationToReservationDto(newReservation);
        }

    throw  new OperationNotAllowed(String.format("Reservation was not successful!"));
    }

    @Override
    public Boolean canceleReservation(String authorization, Long id) {
        Claims claims = tokenService.parseToken(authorization.split(" ")[1]);
        String roleName = claims.get("role",String.class);
        String emailCancel = claims.get("email",String.class);

        if (roleName.equals("ROLE_ADMIN")){
            new OperationNotAllowed(String.format("Role admin can not canc"));
        }

        Reservation reservation = reservationRepository.findById(id).
                orElseThrow(()->new NotFoundException(String.format("Reservation with id: %d does not exists.",id)));


        Long clientId = reservation.getUserId();
        Car car = reservation.getCar();

        //--------------------------------------------//
        Duration diff = Duration.between(reservation.getStartDate().toInstant(), reservation.getEndDate().toInstant());
        Integer days = Math.toIntExact(diff.toDays()) + 1; // mozda + 1

        DecrementRentCarDto decrementRentCarDto = new DecrementRentCarDto();
        decrementRentCarDto.setId(clientId);
        decrementRentCarDto.setDays(days);

        jmsTemplate.convertAndSend(decrementRentCarDestination,messageHelper.createTextMessage(decrementRentCarDto));

        //-------------------------------------------//

        CanceledReservationDto canceledReservationDto = new CanceledReservationDto();

        canceledReservationDto.setEmail(reservation.getEmail());
        canceledReservationDto.setCar(car.getModel().getName() + " " + car.getType().getName());

        jmsTemplate.convertAndSend(canceledReservationDestination,messageHelper.createTextMessage(canceledReservationDto));

        if(!emailCancel.equals(reservation.getEmail())){ //ako otkazuje manager posalji i njemu mejl
            CanceledReservationDto canceledReservationDto1 = new CanceledReservationDto();

            canceledReservationDto1.setEmail(emailCancel);
            canceledReservationDto1.setCar(car.getModel().getName() + " " + car.getType().getName());

            jmsTemplate.convertAndSend(canceledReservationDestination,messageHelper.createTextMessage(canceledReservationDto1));
        }

        //-------------------------------------------//


        reservationRepository.delete(reservation);


        //Refresujem vrednosti ako nema ni jedne rezervacije za ta kola
        List<Reservation> reservations = new ArrayList<>();
        reservationRepository.findAll().forEach(oldReservation -> {
            if (oldReservation.getCar().equals(car)) {
                reservations.add(oldReservation);
            }
        });

        if(reservations.isEmpty()){
            car.setReserved(false);
            carRepository.save(car);
        }

        return true;
    }
}
