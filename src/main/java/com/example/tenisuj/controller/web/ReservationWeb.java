package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.Reservation;
import com.example.tenisuj.service.ReservationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class ReservationWeb {

    private final ReservationService reservationService;

    @Autowired
    public ReservationWeb(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservation")
    public String showReservationForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        return "reservation-form";
    }
    @PostMapping("/reservation")
    public String makeReservation(@Valid @ModelAttribute("reservation") Reservation reservation,BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()) {
            return "reservation-form";
        }

        if (reservationService.isAvailable(reservation.getPlace(),reservation.getDate(),reservation.getStartTime(),reservation.getEndTime())){
            reservationService.createReservation(reservation.getPlace(),reservation.getDate(),reservation.getStartTime(),reservation.getEndTime());
            log.info("Reservation created");
            model.addAttribute("message", "Reservation successfully made");
        }else {
            log.info("Reservation not available");
            model.addAttribute("message", "Reservation not available");
        }
        return "reservation-result";
    }
}
