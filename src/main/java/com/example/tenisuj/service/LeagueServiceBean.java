package com.example.tenisuj.service;

import com.example.tenisuj.model.League;
import com.example.tenisuj.model.Match;
import com.example.tenisuj.model.Player;
import com.example.tenisuj.repository.LeagueRepository;
import com.example.tenisuj.repository.MatchRepository;
import com.example.tenisuj.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class LeagueServiceBean implements LeagueService {

    private final LeagueRepository leagueRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public LeagueServiceBean(LeagueRepository leagueRepository, PlayerRepository playerRepository, MatchRepository matchRepository) {
        this.leagueRepository = leagueRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public void addLeague(String leagueId, String leagueName) {
        if (leagueRepository.existsByName(leagueName)) {
            throw new IllegalArgumentException("League already exists!");
        }
        League league = new League(UUID.randomUUID().toString(), leagueName, null, null);
        log.info("Adding league {}", leagueName);
        leagueRepository.save(league);

    }

    @Override
    public List<League> getAllLeagues() {
        return leagueRepository.findAll().stream().toList();
    }

    @Override
    public League getLeague(String leagueId) {
        return leagueRepository
                .findById(leagueId)
                .orElseThrow(() -> new IllegalArgumentException("League not found"));
    }

    @Override
    public void deleteLeague(String leagueId) {
        if (!leagueRepository.existsById(leagueId)) {
            throw new IllegalArgumentException("League does not exists!");
        }
        leagueRepository.deleteById(leagueId);
        log.info("Deleted league {}", leagueId);
    }

    @Override
    public League addPlayerToLeague(String leagueId, String playerId) {
        var league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new IllegalArgumentException("League not found"));
        var player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        league.getPlayers().add(player);
        player.setLeagueStatus(true);
        player.setLeagueId(leagueId);
        log.info("Adding player to league {}", league);
        playerRepository.save(player);
        leagueRepository.save(league);
        return league;
    }

    @Override
    public List<Match> leagueMatchGenerator(String leagueId) {

        List<Match> matchList = new ArrayList<>();

        var league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new IllegalArgumentException("League not found"));

        List<Player> playerList = league.getPlayers();

        for (int i = 0; i < playerList.size(); i++) {
            for (int j = i + 1; j < playerList.size(); j++) {
                Player player1 = playerList.get(i);
                Player player2 = playerList.get(j);
                if (player1.equals(player2)) {
                    continue;
                }
                matchList.add(new Match(UUID.randomUUID().toString(), player1, player2, null, null, null, null, null, null, null, null, null, null, null, null, null, null,leagueId));

            }
        }


        for (Match match : matchList) {
            matchRepository.save(match);
        }

        league.setMatches(matchList);
        leagueRepository.save(league);

        log.info("League match generator {}", matchList);
        return matchList;
    }

    @Override
    public List<Player> getPlayersSortedByRating(String leagueId) {
        return playerRepository.findByLeagueIdOrderByRatingDesc(leagueId);
    }
}
