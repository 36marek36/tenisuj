package com.example.tenisuj.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationTimeSlot {
    private LocalTime time;
    private boolean reserved;
}
