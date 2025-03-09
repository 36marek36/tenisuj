package com.example.tenisuj.service;

import java.time.LocalDateTime;

public interface ReservationService {

    boolean isAvailable(String place, LocalDateTime startTime,LocalDateTime endTime);
    void createReservation(String place, LocalDateTime startTime,LocalDateTime endTime);
}
