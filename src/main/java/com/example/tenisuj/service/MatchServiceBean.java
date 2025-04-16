package com.example.tenisuj.service;

import com.example.tenisuj.model.Match;
import com.example.tenisuj.model.Player;
import com.example.tenisuj.model.enums.Location;
import com.example.tenisuj.model.enums.MatchStatus;
import com.example.tenisuj.repository.MatchRepository;
import com.example.tenisuj.repository.PlayerRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MatchServiceBean implements MatchService {

    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final PlayerService playerService;

    @Autowired
    public MatchServiceBean(MatchRepository matchRepository, PlayerRepository playerRepository, PlayerService playerService) {

        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
        this.playerService = playerService;
    }

    @Override
    public Match addMatch(String player1Id, String player2Id) {

        Player player1 = playerRepository.findById(player1Id)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        Player player2 = playerRepository.findById(player2Id)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Match match = new Match(UUID.randomUUID().toString(), player1, player2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, MatchStatus.CREATED);

        matchRepository.save(match);
        log.info("Match added");
        return match;
    }

    @Override
    public Match getMatch(String matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));
        return match;
    }

    @Override
    public List<Match> findAllPlayerMatches(String playerId) {
        return matchRepository.findAllPlayerMatches(playerId);
    }

    @Override
    public List<Match> findWonPlayerMatches(String playerId) {
        return matchRepository.findWonPlayerMatches(playerId);
    }

    @Override
    public List<Match> getMatches(String playerName) {
        if (playerName != null) {
            return matchRepository.search(playerName);
        }
        return matchRepository.findAll().stream().toList();
    }

    @Override
    public void deleteMatch(@NonNull String matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new RuntimeException("Match not found");
        }
        Match match = getMatch(matchId);
        if (match.getLeagueId() != null) {
            throw new RuntimeException("League match can not be deleted");
        }
        matchRepository.deleteById(matchId);
        log.info("Match deleted");
    }

    @Override
    public Match addLocation(String matchId, Location location, LocalDateTime dateTime) {

        var match = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new UsernameNotFoundException("Match not found"));

        match.setLocation(location);
        match.setDateTime(dateTime);

        matchRepository.save(match);
        log.info("Match location, date and time updated: {}", match.getId());
        return match;
    }

    @Override
    public Match addResult(String matchId,
                           Integer player1_set1,
                           Integer player2_set1,
                           Integer player1_set2,
                           Integer player2_set2,
                           Integer player1_set3,
                           Integer player2_set3,
                           Integer player1_set4,
                           Integer player2_set4,
                           Integer player1_set5,
                           Integer player2_set5,
                           String scratchedPlayerId,
                           String winnerPlayerId) {

        var match = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new UsernameNotFoundException("Match not found"));

        // Validate set results
        Integer[] player1Points = {player1_set1, player1_set2, player1_set3, player1_set4, player1_set5};
        Integer[] player2Points = {player2_set1, player2_set2, player2_set3, player2_set4, player2_set5};

        for (int i = 0; i < player1Points.length; i++) {
            if ((player1Points[i] != null && player2Points[i] == null) || (player1Points[i] == null && player2Points[i] != null)) {
                throw new RuntimeException("Both players' points must be provided for each set");
            }
            if (i > 0 && (player1Points[i] != null || player2Points[i] != null) && (player1Points[i - 1] == null || player2Points[i - 1] == null)) {
                throw new RuntimeException("Earlier set results must be provided if later set results are entered");
            }
        }

        Player scratched = null;

        if (scratchedPlayerId != null) {
            if (match.getPlayer1().getId().equals(scratchedPlayerId) || match.getPlayer2().getId().equals(scratchedPlayerId)) {
                scratched = playerRepository.findById(scratchedPlayerId)
                        .orElseThrow(() -> new RuntimeException("Player not found"));
                match.setScratched(scratched);
            } else {
                throw new RuntimeException("Player not participating in match");
            }
        }

        if (scratched != null) {
            // Determine winner based on scratching player
            if (scratched.getId().equals(match.getPlayer1().getId())) {
                match.setWinner(match.getPlayer2());
            } else if (scratched.getId().equals(match.getPlayer2().getId())) {
                match.setWinner(match.getPlayer1());
            }
        }

        if (scratched == null) {
            // Determine winner based on points in the last set with points
            int lastSetIndex = -1;
            for (int i = player1Points.length - 1; i >= 0; i--) {
                if (player1Points[i] != null && player2Points[i] != null) {
                    lastSetIndex = i;
                    break;
                }
            }
            if (lastSetIndex == -1) {
                throw new RuntimeException("No valid sets found to determine the winner");
            }
            if (player1Points[lastSetIndex] > player2Points[lastSetIndex]) {
                match.setWinner(match.getPlayer1());
            } else if (player2Points[lastSetIndex] > player1Points[lastSetIndex]) {
                match.setWinner(match.getPlayer2());
            }
        }

        match.setPlayer1_set1(player1_set1);
        match.setPlayer2_set1(player2_set1);
        match.setPlayer1_set2(player1_set2);
        match.setPlayer2_set2(player2_set2);
        match.setPlayer1_set3(player1_set3);
        match.setPlayer2_set3(player2_set3);
        match.setPlayer1_set4(player1_set4);
        match.setPlayer2_set4(player2_set4);
        match.setPlayer1_set5(player1_set5);
        match.setPlayer2_set5(player2_set5);

        if (match.getLeagueId() != null) {
            match.setStatus(MatchStatus.PENDING);
        } else {
            match.setStatus(MatchStatus.APPROVED);
        }

        matchRepository.save(match);

        playerService.updateRating(match.getPlayer1().getId());
        playerService.updateRating(match.getPlayer2().getId());

        log.info("Match result updated: {}", match.getId());
        return match;
    }

    @Override
    public void approveMatch(String matchId) {
        Match match = getMatch(matchId);
        match.setStatus(MatchStatus.APPROVED);
        if (match.getLeagueId() != null) {
            playerService.updateRatingInLeague(match.getPlayer1().getId());
            playerService.updateRatingInLeague(match.getPlayer2().getId());
        }
        matchRepository.save(match);
    }

    @Override
    public void rejectMatch(String matchId) {
        Match match = getMatch(matchId);
        match.setStatus(MatchStatus.REJECTED);
        match = new Match(matchId, match.getPlayer1(), match.getPlayer2(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, match.getLeagueId(), match.getStatus());
        matchRepository.save(match);

    }

    @Override
    public List<Match> getCreatedMatches() {
        return matchRepository.findMatchesByStatus(MatchStatus.CREATED);
    }
}
