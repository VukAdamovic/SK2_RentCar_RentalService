package com.example.SK_Project2.RentalCarService.scheduler;

import com.example.SK_Project2.RentalCarService.domain.Reservation;
import com.example.SK_Project2.RentalCarService.dto.notifications.ReminderDto;
import com.example.SK_Project2.RentalCarService.messageHelper.MessageHelper;
import com.example.SK_Project2.RentalCarService.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SimpleScheduler {
    private ReservationRepository reservationRepository;
    private JmsTemplate jmsTemplate;
    private MessageHelper messageHelper;
    private String reminderReservationDestination;

    public SimpleScheduler(ReservationRepository reservationRepository, JmsTemplate jmsTemplate, MessageHelper messageHelper,
                           @Value("${destination.reminderReservation}")String reminderReservationDestination) {
        this.reservationRepository = reservationRepository;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.reminderReservationDestination = reminderReservationDestination;
    }

    @Scheduled(fixedDelay = 60000, initialDelay = 60000)
    public void reminder(){
        List<Reservation> reservations = new ArrayList<>();
        Date now = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        reservationRepository.findAll().forEach(reservation -> {

            Duration diff = Duration.between(reservation.getStartDate().toInstant(),now.toInstant());
            Integer days = Math.toIntExact(diff.toDays())  + 1;  // + 1 mozda

            if (days <= 3 && reservation.getThreeDaysReminder().equals("NOT_SEND")) {
                reservations.add(reservation);
            }
        });

        for(Reservation reservation : reservations){
            //kreiram taj dto koj cu da saljem
            ReminderDto reminderDto = new ReminderDto();

            reminderDto.setEmail(reservation.getEmail());
            reminderDto.setCar(reservation.getCar().getModel().getName() + " " + reservation.getCar().getType().getName());

            jmsTemplate.convertAndSend(reminderReservationDestination,messageHelper.createTextMessage(reminderDto));
            reservation.setThreeDaysReminder("RECEIVED");
            reservationRepository.save(reservation);
        }
    }
}
