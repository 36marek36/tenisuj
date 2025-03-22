package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.Reservation;
import com.example.tenisuj.model.enums.Location;
import com.example.tenisuj.repository.ReservationRepository;
import com.example.tenisuj.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/tenisuj")
public class TenisujWeb {

    private final UserService userService;
    private final ReservationRepository reservationRepository;

    public TenisujWeb(UserService userService, ReservationRepository reservationRepository) {
        this.userService = userService;
        this.reservationRepository = reservationRepository;
    }


    @GetMapping("/")
    public String tenisuj(Model model, Principal principal) {
        setDefaultValues(model, principal);
        List<Reservation> court1Reservations = reservationRepository.findApprovedReservationsByDateAndPlace(LocalDate.now(), Location.Court1);
        List<Reservation> court2Reservations = reservationRepository.findApprovedReservationsByDateAndPlace(LocalDate.now(), Location.Court2);
        List<Reservation> court3Reservations = reservationRepository.findApprovedReservationsByDateAndPlace(LocalDate.now(), Location.Court3);
        model.addAttribute("court1Reservations", court1Reservations.size());
        model.addAttribute("court2Reservations", court2Reservations.size());
        model.addAttribute("court3Reservations", court3Reservations.size());
        model.addAttribute("today", LocalDate.now());
        return "tenisuj";
    }


    public void setDefaultValues(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Tenisuj-sk");
        if (principal != null) {
            userService.addUserAndPlayerToModel(principal.getName(), model);
        }
    }
}
