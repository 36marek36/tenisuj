//package com.example.tenisuj.controller.web;
//
//import com.example.tenisuj.model.Player;
//import com.example.tenisuj.model.User;
//import com.example.tenisuj.service.LeagueService;
//import com.example.tenisuj.service.MatchService;
//import com.example.tenisuj.service.PlayerService;
//import com.example.tenisuj.service.UserService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.security.Principal;
//
//@Controller
//@RequestMapping("/tenisuj")
//public class Tenisuj {
//    private final UserService userService;
//    private final PlayerService playerService;
//    private final LeagueService leagueService;
//    private final MatchService matchService;
//
//    public Tenisuj(UserService userService, PlayerService playerService, LeagueService leagueService, MatchService matchService) {
//        this.userService = userService;
//        this.playerService = playerService;
//        this.leagueService = leagueService;
//        this.matchService = matchService;
//    }
//
//    @GetMapping("/")
//    public String tenisuj(Model model,Principal principal) {
//        setDefaultValues(model, principal);
//        if (principal!=null) {
//            User user = userService.getUser(principal.getName());
//            if (user.getPlayer() != null) {
//                Player player = playerService.getPlayerById(user.getPlayer().getId());
//                model.addAttribute("player", player);
//                model.addAttribute("myMatches",matchService.findAllPlayerMatches(player.getId()));
//                if (player.getLeagueStatus()){
//                    model.addAttribute("league", leagueService.getLeague(player.getLeagueId()));
//                }
//            }
//            model.addAttribute("user", user.getUsername());
//        }
//        return "tenisuj";
//    }
//
//
//    public void setDefaultValues(Model model, Principal principal) {
//        model.addAttribute("pageTitle", "Tenisuj-sk");
//        if (principal != null) {
//            userService.addUserAndPlayerToModel(principal.getName(), model);
//        }
//    }
//}
