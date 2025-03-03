package com.example.tenisuj.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity(name = "players")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Player {

    @Id
    private String id;

    @NotBlank(message = "First name can not be empty")
    private String firstName;
    @NotBlank(message = "Last name can not be empty")
    private String lastName;

    @Email(message = "Please enter a valid email")
    private String email;

    private String gender;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    private Boolean leagueStatus;

    private String leagueId;

    private String hand;

    private int rating;

    private int leagueRating;

    private LocalDate registrationDate;

}
