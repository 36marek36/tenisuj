package com.example.tenisuj.controller.web;


import com.example.tenisuj.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/tenisuj")
public class TenisujWeb {

    private final UserService userService;
    private final HomeService homeService;
    private final MatchService matchService;
    private final ReservationService reservationService;

    public TenisujWeb(UserService userService, HomeService homeService, MatchService matchService, ReservationService reservationService) {
        this.userService = userService;
        this.homeService = homeService;
        this.matchService = matchService;
        this.reservationService = reservationService;
    }


    @GetMapping("/")
    public String tenisuj(Model model, Principal principal) {
        setDefaultValues(model, principal);
        homeService.initializeVotes();
        model.addAttribute("home", homeService.getHome());
        addVotesToModel(model);
        return "tenisuj";
    }
    @PostMapping("/vote")
    public String submitVote(@RequestParam(required = false) String name,Model model){
        if (name==null||name.isEmpty()){
        model.addAttribute("error","Choose one of the options");
        addVotesToModel(model);
        return "tenisuj";
        }
        homeService.addVote(name);
        addVotesToModel(model);
        model.addAttribute("success", homeService.getHome());
        return "tenisuj";
    }
    @GetMapping("/resetVotes")
    public String resetVotes(Model model){
        homeService.resetVotes();
        model.addAttribute("success", "Votes reset successfully");
        homeService.initializeVotes();
        addVotesToModel(model);
        return "tenisuj";
    }

    private void addVotesToModel(Model model){
        String[] players = {"Roger", "Rafa", "Novak", "Marek"};
        for (String player : players) {
            model.addAttribute("votes"+player, homeService.getVotes(player));
        }
    }


    public void setDefaultValues(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Tenisuj-sk");
        model.addAttribute("matchesSize", matchService.getCreatedMatches().size());
        model.addAttribute("reservationsSize", reservationService.getCreatedReservations().size());
        if (principal != null) {
            userService.addUserAndPlayerToModel(principal.getName(), model);
        }
    }
}
