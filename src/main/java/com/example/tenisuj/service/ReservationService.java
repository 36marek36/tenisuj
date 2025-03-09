package com.example.tenisuj.service;

import java.time.LocalDateTime;

public interface ReservationService {

    boolean isAvailable(String place, LocalDateTime dateTime);
    void createReservation(String place, LocalDateTime dateTime);
}
