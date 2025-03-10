package com.example.tenisuj.controller.rest;

import com.example.tenisuj.model.Reservation;
import com.example.tenisuj.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/reservations")
public class ReservationApi {
    private final ReservationService reservationService;

    @Autowired
    public ReservationApi(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/create")
    ResponseEntity<String> addReservation(@RequestBody Reservation reservation) {
        if (reservationService.isAvailable(reservation.getPlace(), reservation.getDate(), reservation.getStartTime(), reservation.getEndTime())) {
            reservationService.createReservation(reservation.getPlace(), reservation.getDate(), reservation.getStartTime(), reservation.getEndTime(), reservation.getCustomer());
            return ResponseEntity.ok("Reservation successfully made");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Reservation not available");
        }
    }

    @GetMapping("/")
    ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        log.info("Reservations found: {}", reservations);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteReservation(@PathVariable("id") String id) {
        reservationService.deleteReservation(id);
        log.info("Reservation successfully deleted");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
