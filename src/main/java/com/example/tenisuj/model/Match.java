package com.example.tenisuj.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Match {

    @Id
    private String id;

    @ManyToOne
    private Player player1;

    @ManyToOne
    private Player player2;

    private String location;

    @DateTimeFormat
    private LocalDateTime dateTime;

    private Integer player1_set1;
    private Integer player2_set1;
    private Integer player1_set2;
    private Integer player2_set2;
    private Integer player1_set3;
    private Integer player2_set3;
    private Integer player1_set4;
    private Integer player2_set4;
    private Integer player1_set5;
    private Integer player2_set5;

    @ManyToOne
    private Player scratched;

    @ManyToOne
    private Player winner;

    private String leagueId;

    private String status;

//    private boolean confirmedResult;

}
