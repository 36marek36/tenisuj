package com.example.tenisuj.service;

import com.example.tenisuj.model.League;
import com.example.tenisuj.model.Match;
import com.example.tenisuj.model.Player;
import com.example.tenisuj.model.enums.LeagueStatus;
import com.example.tenisuj.model.enums.MatchStatus;
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
        League league = new League(UUID.randomUUID().toString(), leagueName, null, null,null, LeagueStatus.CREATED);
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
        League league = getLeague(leagueId);
        List<Match> matches = league.getMatches();
        for (Match match : matches) {
            match.setLeagueId(null);
            matchRepository.save(match);
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
        if (player.getLeagueId()!=null && !player.getLeagueId().equals(leagueId)) {
            throw new IllegalArgumentException("Player is in a different league!");
        }
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

//        if (!league.getMatches().isEmpty()) {
//            throw new IllegalStateException("League is in progress!");
//        }
        if (league.getStatus().equals(LeagueStatus.ACTIVE)){
            throw new IllegalStateException("League is in progress!");
        }

        List<Player> playerList = league.getPlayers();

        for (int i = 0; i < playerList.size(); i++) {
            for (int j = i + 1; j < playerList.size(); j++) {
                Player player1 = playerList.get(i);
                Player player2 = playerList.get(j);
                if (player1.equals(player2)) {
                    continue;
                }
                matchList.add(new Match(UUID.randomUUID().toString(), player1, player2, null, null, null, null, null, null, null, null, null, null, null, null, null, null,leagueId, MatchStatus.CREATED));

            }
        }


        for (Match match : matchList) {
            matchRepository.save(match);
        }

        league.setStatus(LeagueStatus.ACTIVE);
        league.setMatches(matchList);
        leagueRepository.save(league);

        log.info("League match generator {}", matchList);
        return matchList;
    }

    @Override
    public List<Player> getPlayersSortedByLeagueRating(String leagueId) {
        return playerRepository.findByLeagueIdOrderByLeagueRatingDesc(leagueId);
    }

    @Override
    public League deletePlayerFromLeague(String leagueId, String playerId) {
        var league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new IllegalArgumentException("League not found"));
        var player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        league.getPlayers().remove(player);
        player.setLeagueStatus(false);
        player.setLeagueId(null);
        log.info("Removing player from league {}", league);
        playerRepository.save(player);
        leagueRepository.save(league);
        return league;
    }

    @Override
    public List<Player> playersWithoutLeague() {
        return playerRepository.findByLeagueIdIsNull();
    }

    @Override
    public void finishLeague(String leagueId) {

        var league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new IllegalArgumentException("League not found"));

        if (league.getStatus().equals(LeagueStatus.FINISHED)) {
            throw new IllegalStateException("The League has already been finished!");
        }

        if (!league.getStatus().equals(LeagueStatus.ACTIVE)) {
            throw new IllegalArgumentException("League is not in progress!");
        }

        List<Player> playerList = league.getPlayers();
        Player winner = playerRepository.findByLeagueIdOrderByLeagueRatingDesc(leagueId).getFirst();

        for (Player player : playerList) {

            player.setLeagueStatus(false);
            player.setLeagueId(null);
            player.setLeagueRating(0);
            playerRepository.save(player);
        }

        league.setWinner(winner);
        league.setStatus(LeagueStatus.FINISHED);
        leagueRepository.save(league);
    }

    @Override
    public List<League> getFinishedLeagues() {
        return leagueRepository.findByStatus(LeagueStatus.FINISHED);
    }

    @Override
    public List<League> getNotFinishedLeagues() {
        return leagueRepository.findByStatusNot(LeagueStatus.FINISHED);
    }
}
