package com.example.tenisuj.service;

import com.example.tenisuj.model.Player;
import com.example.tenisuj.repository.MatchRepository;
import com.example.tenisuj.repository.PlayerRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PlayerServiceBean implements PlayerService {

    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public PlayerServiceBean(PlayerRepository playerRepository, MatchRepository matchRepository) {
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public Player addPlayer(@NonNull String firstName, @NonNull String lastName, String email, String gender, LocalDate birthday, Boolean leagueStatus, String hand, int rating,int leagueRating, LocalDate registrationDate) {

        registrationDate = LocalDate.now();
        leagueStatus=false;

        if (gender.equalsIgnoreCase("MALE")) {
            gender = "Male";
        } else if (gender.equalsIgnoreCase("FEMALE")) {
            gender = "Female";
        } else {
            gender = "Other";
        }
        if (hand.equalsIgnoreCase("LEFT")) {
            hand = "Left";
        } else if (hand.equalsIgnoreCase("RIGHT")) {
            hand = "Right";
        } else {
            hand = "Unknown";
        }

        var player = new Player(UUID.randomUUID().toString(), firstName, lastName, email, gender, birthday, leagueStatus,null, hand, rating,leagueRating, registrationDate);
        playerRepository.save(player);
        log.info("Adding player: {}", player.getId());
        return player;
    }

    @Override
    public List<Player> getAllPlayers(String keyword) {
        if (keyword != null) {
            return playerRepository.search(keyword);
        }
        return playerRepository.findAll().stream().toList();
    }

    @Override
    public Player getPlayerById(@NonNull String id) {

        return playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
    }

    @Override
    public void deletePlayer(@NonNull String id) {
        if (!playerRepository.existsById(id)) {
            throw new IllegalArgumentException("Player not found");
        }
        playerRepository.deleteById(id);
        log.info("Deleted player: {}", playerRepository.findById(id));

    }

    @Override
    public Player editPlayer(@NonNull String id,
                             @NonNull String firstName,
                             @NonNull String lastName,
                             String email,
                             String gender,
                             LocalDate birthday,
                             Boolean leagueStatus,
                             String hand,
                             int rating) {

        var player = playerRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Player not found"));

        player.setFirstName(firstName);
        player.setLastName(lastName);

        if (StringUtils.hasText(email)) {
            player.setEmail(email);
        } else {
            player.setEmail(player.getEmail());
        }

        if (StringUtils.hasText(gender)) {
            player.setGender(gender);
        } else {
            player.setGender(player.getGender());
        }

        if (birthday != null) {
            player.setBirthDate(birthday);
        } else {
            player.setBirthDate(player.getBirthDate());
        }

        if (StringUtils.hasText(hand)) {
            player.setHand(hand);
        } else {
            player.setHand(player.getHand());
        }

        player.setLeagueStatus(player.getLeagueStatus());
        player.setRating(player.getRating());

        playerRepository.save(player);
        log.info("Player updated: {}", player.getId());
        return player;
    }

    @Override
    public int updateRating(String playerId) {
        var player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        player.setRating(100 / matchRepository.findAllPlayedPlayerMatches(playerId).size() * matchRepository.findWonPlayerMatches(playerId).size());
        playerRepository.save(player);
        log.info("Updated player rating: {}", player);
        return player.getRating();
    }

    @Override
    public int updateRatingInLeague(String playerId) {
        var player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        player.setLeagueRating(100 / matchRepository.findAllPlayedPlayerMatchesInLeague(playerId,player.getLeagueId()).size() * matchRepository.findWonPlayerMatchesInLeague(playerId,player.getLeagueId()).size());
        playerRepository.save(player);
        log.info("Updated player rating in league: {}", player);
        return player.getLeagueRating();
    }

}
