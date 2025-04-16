package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.User;
import com.example.tenisuj.model.enums.Role;
import com.example.tenisuj.service.MatchService;
import com.example.tenisuj.service.ReservationService;
import com.example.tenisuj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserWeb {

    private final UserService userService;
    private final MatchService matchService;
    private final ReservationService reservationService;

    public UserWeb(UserService userService, MatchService matchService, ReservationService reservationService) {
        this.userService = userService;
        this.matchService = matchService;
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public String getAllUsers(Model model, Principal principal,
                              @RequestParam(value = "successMessage", required = false) String message,
                              @RequestParam(value = "errorMessage", required = false) String error) {
        setDefaultValues(model, principal);
        if (message != null) {
            model.addAttribute("successMessage", message);
        }
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/{id}")
    public String showEditUserForm(@PathVariable("id") String userName, Model model, Principal principal) {
        setDefaultValues(model, principal);
        model.addAttribute("editUser", userService.getUser(userName));
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping("/{id}")
    public String editUser(@PathVariable("id") String userName, @ModelAttribute("editUser") User user) {
        userService.updateUser(userName, user.getRole(), null, null);
        log.info("user edited {}", userName);
        return "redirect:/users/";
    }

    @PostMapping("/userDelete")
    public String deleteUser(@RequestParam String userName, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(userName);

        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users/";
        }
      redirectAttributes.addFlashAttribute("successMessage", "User "+userName+" deleted.");
      return "redirect:/users/";
    }

    private void setDefaultValues(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Users");
        model.addAttribute("matchesSize", matchService.getCreatedMatches().size());
        model.addAttribute("reservationsSize", reservationService.getCreatedReservations().size());
        if (principal != null) {
            userService.addUserAndPlayerToModel(principal.getName(), model);
        }
    }
}
