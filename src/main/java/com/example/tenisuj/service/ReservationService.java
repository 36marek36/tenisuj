package com.example.tenisuj.service;

import com.example.tenisuj.model.Match;
import com.example.tenisuj.model.Reservation;
import com.example.tenisuj.model.ReservationTimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationService {

    boolean isAvailable(String place, LocalDate date, LocalTime startTime, LocalTime endTime);
    void createReservation(String place, LocalDate date, LocalTime startTime, LocalTime endTime, String customer, Match match);
    List<Reservation> getAllReservations();
    void deleteReservation(String id);
    void approveReservation(String reservationId);
    List<Reservation> getAllPlayerReservation(String playerId);
    boolean isTimeReserved (LocalTime time,List<Reservation> reservations);
    List<ReservationTimeSlot> generateTimeSlots (List<Reservation> reservations, LocalDate date);
}
