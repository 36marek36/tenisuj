package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.Reservation;
import com.example.tenisuj.model.enums.Location;
import com.example.tenisuj.repository.ReservationRepository;
import com.example.tenisuj.service.MatchService;
import com.example.tenisuj.service.ReservationService;
import com.example.tenisuj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeWeb {
    private final UserService userService;
    private final ReservationRepository reservationRepository;
    private final MatchService matchService;
    private final ReservationService reservationService;

    @Autowired
    public HomeWeb(UserService userService, ReservationRepository reservationRepository, MatchService matchService, ReservationService reservationService) {
        this.userService = userService;
        this.reservationRepository = reservationRepository;
        this.matchService = matchService;
        this.reservationService = reservationService;
    }

    @GetMapping("/home")
    public String player(Model model, Principal principal) {
        setDefaultValues(model, principal);
        List<Reservation> court1Reservations = reservationRepository.findApprovedReservationsByDateAndPlace(LocalDate.now(), Location.Court1);
        List<Reservation> court2Reservations = reservationRepository.findApprovedReservationsByDateAndPlace(LocalDate.now(), Location.Court2);
        List<Reservation> court3Reservations = reservationRepository.findApprovedReservationsByDateAndPlace(LocalDate.now(), Location.Court3);
        model.addAttribute("court1Reservations", court1Reservations.size());
        model.addAttribute("court2Reservations", court2Reservations.size());
        model.addAttribute("court3Reservations", court3Reservations.size());
        model.addAttribute("today", LocalDate.now());
        return "home";
    }

    public void setDefaultValues(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Home");
        model.addAttribute("matchesSize", matchService.getCreatedMatches().size());
        model.addAttribute("reservationsSize", reservationService.getCreatedReservations().size());
        if (principal != null) {
            userService.addUserAndPlayerToModel(principal.getName(), model);
        }
    }
}
