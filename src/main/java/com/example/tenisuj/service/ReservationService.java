package com.example.tenisuj.service;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationService {

    boolean isAvailable(String place, LocalDate date, LocalTime startTime, LocalTime endTime);
    void createReservation(String place,LocalDate date, LocalTime startTime,LocalTime endTime);
}
