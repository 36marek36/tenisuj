package com.example.tenisuj.service;

import com.example.tenisuj.model.Reservation;
import com.example.tenisuj.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ReservationServiceBean implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceBean(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    @Override
    public boolean isAvailable(String place,LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(place,date, startTime, endTime);
        return conflictingReservations.isEmpty();
    }

    @Override
    public void createReservation(String place,LocalDate date, LocalTime startTime,LocalTime endTime) {
        Reservation reservation = new Reservation(UUID.randomUUID().toString(), place,date, startTime,endTime);
        log.info("Reservation created: {}", reservation);
        reservationRepository.save(reservation);
    }

}
