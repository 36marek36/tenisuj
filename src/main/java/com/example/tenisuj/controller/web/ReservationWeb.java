package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.Reservation;
import com.example.tenisuj.model.ReservationTimeSlot;
import com.example.tenisuj.model.User;
import com.example.tenisuj.repository.ReservationRepository;
import com.example.tenisuj.service.MatchService;
import com.example.tenisuj.service.ReservationService;
import com.example.tenisuj.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/reservations")
public class ReservationWeb {

    private final ReservationService reservationService;
    private final UserService userService;
    private final MatchService matchService;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationWeb(ReservationService reservationService, UserService userService, MatchService matchService, ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.matchService = matchService;
        this.reservationRepository = reservationRepository;
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
    public String makeReservation(@Valid @ModelAttribute("reservation") Reservation reservation, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "reservation-form";
        }

        if (reservationService.isAvailable(reservation.getPlace(), reservation.getDate(), reservation.getStartTime(), reservation.getEndTime())) {
            reservationService.createReservation(reservation.getPlace(), reservation.getDate(), reservation.getStartTime(), reservation.getEndTime(), reservation.getCustomer(), reservation.getMatch());
            log.info("Reservation created");
            model.addAttribute("message", "Reservation successfully made");
        } else {
            log.info("Reservation not available");
            model.addAttribute("message", "Reservation not available");
        }
        return "reservation-result";
    }

    @PostMapping("/approveReservation")
    public String approveReservation(@RequestParam String reservationId, Model model) {
        reservationService.approveReservation(reservationId);
        model.addAttribute("message", "Reservation approved");
        return "reservation-result";
    }

    @PostMapping("/rejectReservation")
    public String rejectReservation(@RequestParam String reservationId, Model model) {
        reservationService.deleteReservation(reservationId);
        model.addAttribute("message", "Reservation rejected");
        return "reservation-result";
    }

    @GetMapping("/my_reservations/{playerId}")
    public String showMyReservations(@PathVariable String playerId, Model model, Principal principal) {
        setDefaultValues(model, principal);
        playerId = userService.getUser(principal.getName()).getPlayer().getId();
        model.addAttribute("myReservations", reservationService.getAllPlayerReservation(playerId));
        log.info("My reservations found");
        return "my-reservations";
    }
    @GetMapping("/reservations-graph")
    public String showReservationGraph(Model model, Principal principal) {
        setDefaultValues(model, principal);
        LocalDate today = LocalDate.now();
        List<Reservation> reservations1 = reservationRepository.findApprovedReservationsByDateAndPlace(today, "kurt1");
        List<Reservation> reservations2 = reservationRepository.findApprovedReservationsByDateAndPlace(today, "kurt2");
        List<Reservation> reservations3 = reservationRepository.findApprovedReservationsByDateAndPlace(today, "kurt3");

        List<ReservationTimeSlot> timeSlots1 = reservationService.generateTimeSlots(reservations1, today);
        List<ReservationTimeSlot> timeSlots2 = reservationService.generateTimeSlots(reservations2, today);
        List<ReservationTimeSlot> timeSlots3 = reservationService.generateTimeSlots(reservations3, today);

        model.addAttribute("timeSlots1", timeSlots1);
        model.addAttribute("timeSlots2", timeSlots2);
        model.addAttribute("timeSlots3", timeSlots3);
        return "reservations-graph";
    }

    private void setDefaultValues(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Players");
        if (principal != null) {
            userService.addUserAndPlayerToModel(principal.getName(), model);
        }
    }
}
