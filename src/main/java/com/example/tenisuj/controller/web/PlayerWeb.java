package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.Player;
import com.example.tenisuj.model.dto.PlayerDTO;
import com.example.tenisuj.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/players")
public class PlayerWeb {

    private final PlayerService playerService;

    @Autowired
    public PlayerWeb(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/")
    String getAllPlayers(Model model, @Param("keyword") String keyword) {
        setDefaultValues(model);
        List<Player> players = playerService.getAllPlayers(keyword);
        List<PlayerDTO> playerDTOs = players.stream()
                .map(PlayerDTO::new)
                .collect(Collectors.toList());
        model.addAttribute("players", playerDTOs);
        model.addAttribute("player", new Player());
        model.addAttribute("keyword", keyword);
        return "players";
    }

    @PostMapping("/create")
    String createPlayer(Model model, @ModelAttribute("player") Player player) {
        setDefaultValues(model);
        playerService.addPlayer(player.getFirstName(), player.getLastName(), player.getEmail(), player.getGender(), player.getBirthDate(), player.getLeagueStatus(), player.getHand(), player.getRating(), player.getRegistrationDate());
        return "redirect:/players/";
    }

    @GetMapping("/{id}")
    public String showEditPlayerForm(@PathVariable("id") String playerId, Model model) {
        setDefaultValues(model);
        model.addAttribute("player", playerService.getPlayerById(playerId));
        return "playerEdit";
    }

    @PostMapping("/{id}")
    public String editPlayer(@PathVariable("id") String playerId, Model model, @ModelAttribute("player") Player player) {
        setDefaultValues(model);
        playerService.editPlayer(playerId, player.getFirstName(),player.getLastName(),player.getEmail(),player.getGender(),player.getBirthDate(),player.getLeagueStatus(),player.getHand(),player.getRating());
        log.info("player edited {}", playerId);
        return "redirect:/players/";
    }

    private void setDefaultValues(Model model) {
        model.addAttribute("pageTitle", "Players");
    }
}
