package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.League;
import com.example.tenisuj.model.dto.UpdateLeagueDto;
import com.example.tenisuj.service.LeagueService;
import com.example.tenisuj.service.MatchService;
import com.example.tenisuj.service.ReservationService;
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
    private final MatchService matchService;
    private final ReservationService reservationService;

    @Autowired
    public LeagueWeb(LeagueService leagueService, UserService userService, MatchService matchService, ReservationService reservationService) {
        this.leagueService = leagueService;
        this.userService = userService;
        this.matchService = matchService;
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public String getAllLeagues(Model model, Principal principal) {
        setDefaultValues(model, principal);
        model.addAttribute("leagues", leagueService.getNotFinishedLeagues());

        return "leagues";
    }

    @GetMapping("/archive")
    public String getFinishedLeagues(Model model, Principal principal) {
        setDefaultValues(model, principal);
        model.addAttribute("finishedLeagues", leagueService.getFinishedLeagues());
        return "leaguesArchive";
    }

    @GetMapping("/leagueCreate")
    public String getCreateLeagueForm(Model model, Principal principal) {
        setDefaultValues(model, principal);
        model.addAttribute("league", new League());
        return "leagueCreate";
    }

    @PostMapping("/create")
    public String createLeague(@ModelAttribute("league") League league) {
        leagueService.addLeague(league.getId(), league.getName());
        return "redirect:/leagues/";
    }

    @GetMapping("/add-matches/{id}")
    public String startLeague(@PathVariable("id") String leagueId, RedirectAttributes redirectAttributes) {
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
        model.addAttribute("progress", leagueService.progress(leagueId));
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
    @PostMapping("/deleteLeague")
    public String deleteLeague(@RequestParam String leagueId, RedirectAttributes redirectAttributes) {
        leagueService.deleteLeague(leagueId);
        redirectAttributes.addFlashAttribute("successMessage", "League deleted.");
        return "redirect:/leagues/archive";
    }

    private void setDefaultValues(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Leagues");
        model.addAttribute("matchesSize", matchService.getCreatedMatches().size());
        model.addAttribute("reservationsSize", reservationService.getCreatedReservations().size());
        if (principal != null) {
            userService.addUserAndPlayerToModel(principal.getName(), model);
        }
    }
}