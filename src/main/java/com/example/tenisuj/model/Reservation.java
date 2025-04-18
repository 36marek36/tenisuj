package com.example.tenisuj.model;

import com.example.tenisuj.model.enums.Location;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "reservations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    private String id;

    @NotNull
    private Location place;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private String customer;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    private String status;
}
