package com.example.tenisuj.model;

import com.example.tenisuj.model.enums.LeagueStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "leagues")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class League {
    @Id
    private String id;
    private String name;
    @ManyToMany
    private List<Player> players;
    @OneToMany
    private List<Match> matches;
    @ManyToOne
    private Player winner;

    LeagueStatus status;
}
