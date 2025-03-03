package com.example.tenisuj.service;

import com.example.tenisuj.model.Player;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;

public interface PlayerService {
    Player addPlayer(@NonNull String firstName,
                     @NonNull String lastName,
                     String email,
                     String gender,
                     LocalDate birthday,
                     Boolean leagueStatus,
                     String hand,
                     int rating,
                     int leagueRating,
                     LocalDate registrationDate);

    List<Player> getAllPlayers(String keyword);

    Player getPlayerById(@NonNull String id);

    void deletePlayer(@NonNull String id);

    Player editPlayer(@NonNull String id,
                      @NonNull String firstName,
                      @NonNull String lastName,
                      String email,
                      String gender,
                      LocalDate birthday,
                      Boolean leagueStatus,
                      String hand,
                      int rating);

    int updateRating(String playerId);

    int updateRatingInLeague(String playerId);

}
