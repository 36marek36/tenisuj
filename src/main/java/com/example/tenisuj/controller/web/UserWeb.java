package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.User;
import com.example.tenisuj.model.enums.Role;
import com.example.tenisuj.service.LeagueService;
import com.example.tenisuj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserWeb {

    private final UserService userService;
    private final LeagueService leagueService;

    public UserWeb(UserService userService, LeagueService leagueService) {
        this.userService = userService;
        this.leagueService = leagueService;
    }

    @GetMapping("/")
    public String getAllUsers(Model model, Principal principal) {
        setDefaultValues(model, principal);
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/{id}")
    public String showEditUserForm(@PathVariable("id") String userName, Model model, Principal principal) {
        setDefaultValues(model, principal);
        model.addAttribute("editUser", userService.getUser(userName));
        model.addAttribute("roles", Role.values());
        model.addAttribute("leagues", leagueService.getAllLeagues());
        return "userEdit";
    }

    @PostMapping("/{id}")
    public String editUser(@PathVariable("id") String userName, @ModelAttribute("editUser") User user) {
        userService.updateUser(userName, user.getRole(), user.getPassword(), user.getPlayer().getId());
        log.info("user edited {}", userName);
        return "redirect:/users/";
    }

    private void setDefaultValues(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Users");
        if (principal != null) {
            userService.addUserAndPlayerToModel(principal.getName(), model);
        }
    }
}
