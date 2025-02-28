package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.User;
import com.example.tenisuj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class LoginWeb {

    private final UserService userService;

    @Autowired
    public LoginWeb(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model, Principal principal) {
        setDefaultValues(model, principal);
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model, Principal principal) {
        setDefaultValues(model, principal);
        return "logout";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("user") User user) {
        userService.addUser(user.getUsername(), user.getPassword());
        return "redirect:/login";
    }

    private void setDefaultValues(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Log-in/out");
        if (principal != null) {
            userService.addUserAndPlayerToModel(principal.getName(), model);
        }
    }

}