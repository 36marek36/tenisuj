package com.example.tenisuj.controller.web;

import com.example.tenisuj.service.MatchService;
import com.example.tenisuj.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/matches")
public class MatchWeb {

    private final MatchService matchService;
    private final PlayerService playerService;

    @Autowired
    public MatchWeb(MatchService matchService, PlayerService playerService) {
        this.matchService = matchService;
        this.playerService = playerService;
    }

    @GetMapping("/")
    String getAllMatches(Model model, @Param("playerName") String playerName) {
        setDefaultValues(model);
        model.addAttribute("matches", matchService.getMatches(playerName));
        model.addAttribute("player", playerService.getAllPlayers(null));
        model.addAttribute("playerName", playerName);
        return "matches";
    }

    @GetMapping("/details/{id}")
    String getMatchDetails(@PathVariable("id") String matchId, Model model) {
        setDefaultValues(model);
        model.addAttribute("match", matchService.getMatch(matchId));
        return "matchDetails";
    }

    private void setDefaultValues(Model model) {
        model.addAttribute("pageTitle", "Matches");
    }

}
