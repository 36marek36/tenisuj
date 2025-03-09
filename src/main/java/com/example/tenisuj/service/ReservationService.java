package com.example.tenisuj.service;

import com.example.tenisuj.model.Reservation;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationService {

    boolean isAvailable(String place, LocalDate date, LocalTime startTime, LocalTime endTime);
    void createReservation(String place, LocalDate date, LocalTime startTime, LocalTime endTime);
    List<Reservation> getAllReservations();
    void deleteReservation(String id);
}
