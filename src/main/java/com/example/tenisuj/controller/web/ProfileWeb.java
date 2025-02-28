package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.Player;
import com.example.tenisuj.model.User;
import com.example.tenisuj.service.PlayerService;
import com.example.tenisuj.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        setDefaultValues(model, principal);
        return "profile";
    }

    @GetMapping("/create")
    public String showCreateProfileForm(Model model, Principal principal) {
        setDefaultValues(model, principal);
        model.addAttribute("createPlayer", new Player());
        return "profileCreate";
    }

    @PostMapping("/create")
    String createPlayer(@ModelAttribute("createPlayer") @Valid Player player, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "profileCreate";
        }
        Player savedPlayer = playerService.addPlayer(player.getFirstName(), player.getLastName(), player.getEmail(), player.getGender(), player.getBirthDate(), player.getLeagueStatus(), player.getHand(), player.getRating(), player.getRegistrationDate());
        userService.updateUser(principal.getName(), null, savedPlayer.getId());
        return "redirect:/profile/";
    }

    @GetMapping("/edit")
    public String showEditProfileForm(Model model, Principal principal) {
        setDefaultValues(model, principal);
        model.addAttribute("editPlayer", userService.getUser(principal.getName()).getPlayer());
        return "profileEdit";
    }

    @PostMapping("/edit")
    public String editPlayer(@ModelAttribute("editPlayer") Player player, Principal principal) {
        playerService.editPlayer(userService.getUser(principal.getName()).getPlayer().getId(), player.getFirstName(), player.getLastName(), player.getEmail(), player.getGender(), player.getBirthDate(), player.getLeagueStatus(), player.getHand(), player.getRating());
        return "redirect:/profile/";
    }

    private void setDefaultValues(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Profile");
        if (principal != null) {
            User currentUsername = userService.getUser(principal.getName());
            if (currentUsername != null && currentUsername.getPlayer() != null) {
                model.addAttribute("user", currentUsername);
                model.addAttribute("player", playerService.getPlayerById(currentUsername.getPlayer().getId()));
            } else {
                model.addAttribute("player", null);
            }
        }
    }
}
