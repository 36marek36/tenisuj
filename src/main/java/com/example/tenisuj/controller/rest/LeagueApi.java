package com.example.tenisuj.controller.rest;

import com.example.tenisuj.model.League;
import com.example.tenisuj.model.dto.UpdateLeagueDto;
import com.example.tenisuj.service.LeagueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/leagues")
public class LeagueApi {
    private final LeagueService leagueService;

    @Autowired
    public LeagueApi(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @GetMapping("/")
    public List<League> getLeagues() {
        log.info("getLeagues");
        return leagueService.getAllLeagues();
    }

    @GetMapping("/{id}")
    League getLeague(@PathVariable String id) {
        log.info("getLeague {}", id);
        return leagueService.getLeague(id);
    }

    @PostMapping("/create")
    ResponseEntity<String> createLeague(@RequestBody League league) {
        leagueService.addLeague(league.getId(), league.getName());
        log.info("createLeague");
        return new ResponseEntity<>("League created", HttpStatus.CREATED);
    }

    @PatchMapping("/add_matches/{id}")
    ResponseEntity<String> addMatches(@PathVariable("id") String id, League league) {
        league.setMatches(leagueService.leagueMatchGenerator(id));
        log.info("addMatches");
        return new ResponseEntity<>("League matches added", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteLeague(@PathVariable("id") String id) {
        leagueService.deleteLeague(id);
        log.info("deleteLeague");
        return new ResponseEntity<>("League deleted", HttpStatus.OK);
    }

    @PatchMapping("/{id}/addPlayer")
    ResponseEntity<League> addPlayerToLeague(@PathVariable("id") String id, @RequestBody UpdateLeagueDto league) {
        var updated = leagueService.addPlayerToLeague(id, league.getPlayerId());
        log.info("addPlayerToLeague {}", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}/deletePlayer")
    ResponseEntity<League> deletePlayerFromLeague(@PathVariable("id") String id, @RequestBody UpdateLeagueDto league) {
        var updated = leagueService.deletePlayerFromLeague(id, league.getPlayerId());
        log.info("deletePlayerFromLeague {}", id);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/finish")
    ResponseEntity<String> finishLeague(@PathVariable("id") String id) {
        log.info("finish league {}", id);
        try {
            leagueService.finishLeague(id);
            return ResponseEntity.ok("League finished");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("handleIllegalArgumentException: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
