package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.Reservation;
import com.example.tenisuj.model.User;
import com.example.tenisuj.service.MatchService;
import com.example.tenisuj.service.ReservationService;
import com.example.tenisuj.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/reservations")
public class ReservationWeb {

    private final ReservationService reservationService;
    private final UserService userService;
    private final MatchService matchService;

    @Autowired
    public ReservationWeb(ReservationService reservationService, UserService userService, MatchService matchService) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.matchService = matchService;
    }

    @GetMapping("/")
    public String showReservationForm(Model model, Principal principal) {
        setDefaultValues(model, principal);
        if (principal != null) {
            User user = userService.getUser(principal.getName());
            if (user.getPlayer() != null) {
                String playerName = user.getPlayer().getFirstName() + " " + user.getPlayer().getLastName();
                model.addAttribute("customer", playerName);
                model.addAttribute("myMatches", matchService.findAllPlayerMatches(user.getPlayer().getId()));
            }

        }
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("reservations", reservationService.getAllReservations());

        return "reservation-form";
    }
    @PostMapping("/")
    public String makeReservation(@Valid @ModelAttribute("reservation") Reservation reservation,BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()) {
            return "reservation-form";
        }

        if (reservationService.isAvailable(reservation.getPlace(),reservation.getDate(),reservation.getStartTime(),reservation.getEndTime())){
            reservationService.createReservation(reservation.getPlace(),reservation.getDate(),reservation.getStartTime(),reservation.getEndTime(),reservation.getCustomer(),reservation.getMatch());
            log.info("Reservation created");
            model.addAttribute("message", "Reservation successfully made");
        }else {
            log.info("Reservation not available");
            model.addAttribute("message", "Reservation not available");
        }
        return "reservation-result";
    }
    private void setDefaultValues(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Players");
        if (principal != null) {
            userService.addUserAndPlayerToModel(principal.getName(), model);
        }
    }
}
