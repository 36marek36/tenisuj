package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.Player;
import com.example.tenisuj.model.User;
import com.example.tenisuj.model.dto.PlayerDTO;
import com.example.tenisuj.service.MatchService;
import com.example.tenisuj.service.PlayerService;
import com.example.tenisuj.service.ReservationService;
import com.example.tenisuj.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/players")
public class PlayerWeb {

    private final PlayerService playerService;
    private final UserService userService;
    private final MatchService matchService;
    private final ReservationService reservationService;

    @Autowired
    public PlayerWeb(PlayerService playerService, UserService userService, MatchService matchService, ReservationService reservationService) {
        this.playerService = playerService;
        this.userService = userService;
        this.matchService = matchService;
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    String getAllPlayers(Model model, @Param("keyword") String keyword, Principal principal,
                         @RequestParam(value = "successMessage", required = false) String message,
                         @RequestParam(value = "errorMessage", required = false) String error) {
        setDefaultValues(model, principal);
        if (message != null) {
            model.addAttribute("successMessage", message);
        }
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }
        List<Player> players = playerService.getAllPlayers(keyword);
        List<PlayerDTO> playerDTOs = players.stream()
                .map(PlayerDTO::new)
                .collect(Collectors.toList());
        model.addAttribute("players", playerDTOs);
        model.addAttribute("keyword", keyword);
        return "players";
    }

    @GetMapping("/create")
    String createPlayer(Model model, Principal principal) {
        setDefaultValues(model, principal);
        model.addAttribute("newPlayer", new Player());
        return "playerCreate";
    }

    @PostMapping("/create")
    String createPlayer(@ModelAttribute("newPlayer") @Valid Player player, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "playerCreate";
        }
        playerService.addPlayer(player.getFirstName(), player.getLastName(), player.getEmail(), player.getGender(), player.getBirthDate(), player.getLeagueStatus(), player.getHand(), player.getRating(), player.getLeagueRating(), player.getRegistrationDate());
        return "redirect:/players/";
    }

    @GetMapping("/{id}")
    public String showEditPlayerForm(@PathVariable("id") String playerId, Model model, Principal principal) {
        setDefaultValues(model, principal);
        model.addAttribute("editPlayer", playerService.getPlayerById(playerId));
        return "playerEdit";
    }

    @PostMapping("/{id}")
    public String editPlayer(@PathVariable("id") String playerId, @ModelAttribute("editPlayer") Player player) {
        playerService.editPlayer(playerId, player.getFirstName(), player.getLastName(), player.getEmail(), player.getGender(), player.getBirthDate(), player.getLeagueStatus(), player.getHand(), player.getRating());
        log.info("player edited {}", playerId);
        return "redirect:/players/";
    }

    @PostMapping("/deletePlayer")
    String deletePlayer(@RequestParam String playerId, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByPlayerId(playerId);
            if (user != null) {
                user.setPlayer(null);
            }
            playerService.deletePlayer(playerId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Player cannot be deleted");
            return "redirect:/players/";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Player has been deleted");
        return "redirect:/players/";
    }

    private void setDefaultValues(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Players");
        model.addAttribute("matchesSize", matchService.getCreatedMatches().size());
        model.addAttribute("reservationsSize", reservationService.getCreatedReservations().size());
        if (principal != null) {
            userService.addUserAndPlayerToModel(principal.getName(), model);
        }
    }
}
