package com.example.tenisuj.controller.rest;

import com.example.tenisuj.model.Player;
import com.example.tenisuj.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/rest/players")
public class PlayerApi {
    private final PlayerService playerService;

    @Autowired
    public PlayerApi(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/")
    List<Player> getPlayers(String keyword) {
        log.info("Get all players");
        return playerService.getAllPlayers(keyword);
    }

    @GetMapping("/{id}")
    Player getPlayer(@PathVariable String id) {
        log.info("Get player with id {}", id);
        return playerService.getPlayerById(id);
    }

    @PostMapping("/create")
    ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        Player created = playerService.addPlayer(player.getFirstName(), player.getLastName(), player.getEmail(), player.getGender(), player.getBirthDate(), player.getLeagueStatus(), player.getHand(), player.getRating(),player.getLeagueRating(), player.getRegistrationDate());
        log.info("Created player: {}", created);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deletePlayer(@PathVariable("id") String id) {
        playerService.deletePlayer(id);
        log.info("Deleted player: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    ResponseEntity<String> updatePlayerRating(@PathVariable("id") String id, @RequestBody Player player) {
        player.setRating(playerService.updateRating(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException e) {
        log.error("handleDateTimeParseException: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
