package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.User;
import com.example.tenisuj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class LoginWeb {

    private final UserService userService;

    @Autowired
    public LoginWeb(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model, Principal principal,
                        @RequestParam(value = "successMessage", required = false) String message,
                        @RequestParam(value = "error", required = false) String error) {
        setDefaultValues(model, principal);
        if (message != null) {
            model.addAttribute("loginMessage", message);
        }
        if (error != null) {
            model.addAttribute("loginError", "Invalid username or password.");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Model model, Principal principal) {
        setDefaultValues(model, principal);
        return "logout";
    }

    @GetMapping("/signup")
    public String signup(Model model, Principal principal, @RequestParam(value = "signupError", required = false) String message) {
        setDefaultValues(model, principal);
        if (message != null) {
            model.addAttribute("signupError", message);
        }
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        if (userService.userExists(user.getUsername())) {
            redirectAttributes.addFlashAttribute("signupError", "Username already exists");
            return "redirect:/signup";
        }
        userService.addUser(user.getUsername(), user.getPassword());
        redirectAttributes.addAttribute("successMessage", "User " + user.getUsername() + " created successfully. Please login");
        return "redirect:/login";
    }

    private void setDefaultValues(Model model, Principal principal) {
        model.addAttribute("pageTitle", "Log-in/out");
        if (principal != null) {
            userService.addUserAndPlayerToModel(principal.getName(), model);
        }
    }

}