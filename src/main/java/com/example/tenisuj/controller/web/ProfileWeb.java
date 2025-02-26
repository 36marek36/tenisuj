package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.Player;
import com.example.tenisuj.service.PlayerService;
import com.example.tenisuj.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileWeb {

    private final PlayerService playerService;
    private final UserService userService;

    public ProfileWeb(PlayerService playerService, UserService userService) {
        this.playerService = playerService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String getProfile(Model model, Principal principal) {
        setDefaultValues(model);
        model.addAttribute("user", userService.getUser(principal.getName()));
        model.addAttribute("player",playerService.getPlayerById(userService.getUser(principal.getName()).getPlayer().getId()));
        return "profile";
    }

    @GetMapping("/create")
    public String showCreateProfileForm(Model model) {
        model.addAttribute("player", new Player());
        return "profileCreate";
    }

    @PostMapping("/create")
    String createPlayer(@ModelAttribute("player") Player player, Principal principal) {
        Player savedPlayer = playerService.addPlayer(player.getFirstName(), player.getLastName(), player.getEmail(), player.getGender(), player.getBirthDate(), player.getLeagueStatus(), player.getHand(), player.getRating(), player.getRegistrationDate());
        userService.updateUser(principal.getName(), null, savedPlayer.getId());
        return "redirect:/profile/";
    }

    @GetMapping("/edit")
    public String showEditProfileForm(Model model, Principal principal) {
        model.addAttribute("player", userService.getUser(principal.getName()).getPlayer());
        return "profileEdit";
    }

    @PostMapping("/edit")
    public String editPlayer(Model model, @ModelAttribute("player") Player player, Principal principal) {
        setDefaultValues(model);
        playerService.editPlayer(userService.getUser(principal.getName()).getPlayer().getId(), player.getFirstName(),player.getLastName(),player.getEmail(),player.getGender(),player.getBirthDate(),player.getLeagueStatus(),player.getHand(),player.getRating());
        return "redirect:/profile/";
    }

    private void setDefaultValues(Model model) {
        model.addAttribute("pageTitle", "Profile");
    }
}
