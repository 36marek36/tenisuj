package com.example.tenisuj.controller.web;

import com.example.tenisuj.model.User;
import com.example.tenisuj.model.enums.Role;
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

    public UserWeb(UserService userService) {
        this.userService = userService;
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
        return "userEdit";
    }

    @PostMapping("/{id}")
    public String editUser(@PathVariable("id") String userName, @ModelAttribute("editUser") User user) {
        userService.updateUser(userName, user.getRole(), null, null);
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
