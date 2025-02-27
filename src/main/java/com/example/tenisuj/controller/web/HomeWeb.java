package com.example.tenisuj.controller.web;

import com.example.tenisuj.service.HomeService;
import com.example.tenisuj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeWeb {
    private final HomeService homeService;
    private final UserService userService;

    @Autowired
    public HomeWeb(HomeService homeService, UserService userService) {
        this.homeService = homeService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String player(Model model, Principal principal) {
        setDefaultValues(model, principal);
        model.addAttribute("home", homeService.getHome());

        return "home";
    }

    public void setDefaultValues(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Tenisuj-sk");
        if (principal != null) {
            userService.addUserAndPlayerToModel(principal.getName(), model);
        }
    }
}
