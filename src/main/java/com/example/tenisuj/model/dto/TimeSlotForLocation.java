package com.example.tenisuj.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotForLocation {
    private String name;
    private List<ReservationTimeSlot> reservationTimeSlots;
}
