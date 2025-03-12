package com.example.tenisuj.service;

import com.example.tenisuj.model.Match;
import com.example.tenisuj.model.Reservation;
import com.example.tenisuj.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ReservationServiceBean implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final MatchService matchService;

    @Autowired
    public ReservationServiceBean(ReservationRepository reservationRepository, MatchService matchService) {
        this.reservationRepository = reservationRepository;
        this.matchService = matchService;
    }


    @Override
    public boolean isAvailable(String place,LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(place,date, startTime, endTime);
        return conflictingReservations.isEmpty();
    }

    @Override
    public void createReservation(String place,LocalDate date, LocalTime startTime,LocalTime endTime,String customer,Match match) {
        Reservation reservation = new Reservation(UUID.randomUUID().toString(), place,date, startTime,endTime,customer,match,"pending");
        if (reservation.getMatch() != null) {
            LocalDateTime dateTime = date.atTime(startTime);
            matchService.addLocation(match.getId(), place,dateTime);
        }
        log.info("Reservation created: {}", reservation);
        reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll().stream().toList();
    }

    @Override
    public void deleteReservation(String id) {
        if (!reservationRepository.existsById(id)) {
            throw new IllegalArgumentException("Reservation not found");
        }
        reservationRepository.deleteById(id);
        log.info("Reservation deleted: {}", reservationRepository.findById(id));
    }

    @Override
    public void approveReservation(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservation.setStatus("approved");
        reservationRepository.save(reservation);
    }

    @Override
    public void rejectReservation(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservationRepository.delete(reservation);

    }


}
