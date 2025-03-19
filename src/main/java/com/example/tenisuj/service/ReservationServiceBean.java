package com.example.tenisuj.service;

import com.example.tenisuj.model.Match;
import com.example.tenisuj.model.Player;
import com.example.tenisuj.model.Reservation;
import com.example.tenisuj.model.dto.ReservationTimeSlot;
import com.example.tenisuj.model.enums.Location;
import com.example.tenisuj.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ReservationServiceBean implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final MatchService matchService;
    private final PlayerService playerService;

    @Autowired
    public ReservationServiceBean(ReservationRepository reservationRepository, MatchService matchService, PlayerService playerService) {
        this.reservationRepository = reservationRepository;
        this.matchService = matchService;
        this.playerService = playerService;
    }


    @Override
    public boolean isAvailable(Location place, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(place, date, startTime, endTime);
        return conflictingReservations.isEmpty();
    }

    @Override
    public void createReservation(Location place, LocalDate date, LocalTime startTime, LocalTime endTime, String customer, Match match) {
        Reservation reservation = new Reservation(UUID.randomUUID().toString(), place, date, startTime, endTime, customer, match, "pending");
        if (reservation.getMatch() != null) {
            LocalDateTime dateTime = date.atTime(startTime);
            matchService.addLocation(match.getId(), place, dateTime);
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
    public List<Reservation> getAllPlayerReservation(String playerId) {
        Player player = playerService.getPlayerById(playerId);
        String playerFullName = player.getFirstName() + " " + player.getLastName();
        return reservationRepository.findAllPlayerReservations(playerFullName);
    }

    @Override
    public boolean isTimeReserved(LocalTime time, List<Reservation> reservations) {
        for (Reservation reservation : reservations) {
            if (!time.isBefore(reservation.getStartTime()) && !time.isAfter(reservation.getEndTime())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<ReservationTimeSlot> generateTimeSlotsWithStatus(List<Reservation> reservations, LocalDate date) {
        List<ReservationTimeSlot> timeSlots = new ArrayList<>();

        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                LocalTime time = LocalTime.of(hour, minute);
                boolean reserved = isTimeReserved(time, reservations);
                String status = getReservationStatus(time, reservations); // Získame stav rezervácie

                timeSlots.add(new ReservationTimeSlot(time, reserved, status)); // Pridáme aj status
            }
        }
        return timeSlots;
    }

    @Override
    public String getReservationStatus(LocalTime time, List<Reservation> reservations) {
        // Pre každú rezerváciu skontrolujeme, či jej čas spadá do daného slotu
        for (Reservation reservation : reservations) {
            LocalTime startTime = reservation.getStartTime();
            LocalTime endTime = reservation.getEndTime();

            // Skontrolujeme, či je daný časový slot v intervale medzi startTime a endTime
            if ((time.isAfter(startTime) || time.equals(startTime)) && (time.isBefore(endTime) || time.equals(endTime))) {
                return reservation.getStatus(); // Vráti status rezervácie (pending, approved)
            }
        }
        return "free"; // Ak časový slot nie je obsadený, je free
    }
}
