package com.example.tenisuj.controller.web;


import com.example.tenisuj.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller
@RequestMapping("/tenisuj")
public class TenisujWeb {

    private final UserService userService;
    private final HomeService homeService;

    public TenisujWeb(UserService userService, HomeService homeService) {
        this.userService = userService;
        this.homeService = homeService;
    }


    @GetMapping("/")
    public String tenisuj(Model model, Principal principal) {
        setDefaultValues(model, principal);
        model.addAttribute("home", homeService.getHome());
        return "tenisuj";
    }


    public void setDefaultValues(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Tenisuj-sk");
        if (principal != null) {
            userService.addUserAndPlayerToModel(principal.getName(), model);
        }
    }
}
