package com.example.tenisuj.service;

import com.example.tenisuj.model.League;
import com.example.tenisuj.model.Match;
import com.example.tenisuj.model.Player;

import java.util.List;

public interface LeagueService {
    void addLeague(String leagueId, String leagueName);

    List<League> getAllLeagues();

    League getLeague(String leagueId);

    void deleteLeague(String leagueId);

    League addPlayerToLeague(String leagueId, String playerId);

    List<Match> leagueMatchGenerator(String leagueId);

    List<Player> getPlayersSortedByLeagueRating(String leagueId);

    League deletePlayerFromLeague(String leagueId, String playerId);

    List<Player> playersWithoutLeague();

    void finishLeague(String leagueId);

    List<League> getFinishedLeagues();

    List<League> getNotFinishedLeagues();

    List<Match> getAllLeagueMatches(String leagueId);

    List<Match> getAllPlayedLeagueMatches(String leagueId);

    int progress(String leagueId);

}
