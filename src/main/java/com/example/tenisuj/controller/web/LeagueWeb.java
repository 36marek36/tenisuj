package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.League;
import com.example.tenisuj.model.dto.UpdateLeagueDto;
import com.example.tenisuj.service.LeagueService;
import com.example.tenisuj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/leagues")
@Slf4j
public class LeagueWeb {

    private final LeagueService leagueService;
    private final UserService userService;

    @Autowired
    public LeagueWeb(LeagueService leagueService, UserService userService) {
        this.leagueService = leagueService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String getAllLeagues(Model model, Principal principal) {
        setDefaultValues(model, principal);
        model.addAttribute("leagues", leagueService.getNotFinishedLeagues());
        model.addAttribute("finishedLeagues", leagueService.getFinishedLeagues());
        model.addAttribute("league", new League());
        return "leagues";
    }

    @PostMapping("/create")
    public String createLeague(@ModelAttribute("league") League league) {
        leagueService.addLeague(league.getId(), league.getName());
        return "redirect:/leagues/";
    }

    @GetMapping("/add-matches/{id}")
    public String addMatches(@PathVariable("id") String leagueId, RedirectAttributes redirectAttributes) {
        try{
            leagueService.leagueMatchGenerator(leagueId);
        }catch (IllegalStateException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/leagues/details/" + leagueId;
        }
        return "redirect:/leagues/details/" + leagueId;
    }

    @PostMapping("/finishLeague/{id}")
    public String finishLeague(@PathVariable("id") String leagueId, RedirectAttributes redirectAttributes) {
        try {
            leagueService.finishLeague(leagueId);
        }catch (IllegalStateException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/leagues/details/" + leagueId;
        }
        return "redirect:/leagues/details/" + leagueId;
    }

    @GetMapping("/details/{id}")
    public String getLeague(@PathVariable("id") String leagueId, UpdateLeagueDto updateLeagueDto, Model model, Principal principal) {
        setDefaultValues(model, principal);
        model.addAttribute("league", leagueService.getLeague(leagueId));
        model.addAttribute("playersWL", leagueService.playersWithoutLeague());
        model.addAttribute("updateLeagueDto", updateLeagueDto);
        model.addAttribute("sortedPlayers", leagueService.getPlayersSortedByLeagueRating(leagueId));
        return "leagueDetails";
    }

    @PostMapping("/details/{id}/add")
    public String addPlayerToLeague(@PathVariable("id") String leagueId, UpdateLeagueDto updateLeagueDto,RedirectAttributes redirectAttributes) {
        try {
            leagueService.addPlayerToLeague(leagueId, updateLeagueDto.getPlayerId());
            log.info("add player to league {}", leagueId);
        }catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/leagues/details/" + leagueId;
        }

        return "redirect:/leagues/details/" + leagueId;
    }

    @PostMapping("/details/{id}/remove")
    public String deletePlayerFromLeague(@PathVariable("id") String leagueId, UpdateLeagueDto updateLeagueDto,RedirectAttributes redirectAttributes) {
        try {
            leagueService.deletePlayerFromLeague(leagueId, updateLeagueDto.getPlayerId());
            log.info("remove player from league {}", leagueId);
        }catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/leagues/details/" + leagueId;
        }

        return "redirect:/leagues/details/" + leagueId;
    }

    private void setDefaultValues(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Leagues");
        if (principal != null) {
            userService.addUserAndPlayerToModel(principal.getName(), model);
        }
    }
}