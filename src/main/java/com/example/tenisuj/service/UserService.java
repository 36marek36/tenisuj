package com.example.tenisuj.service;

import com.example.tenisuj.model.User;
import com.example.tenisuj.model.enums.Role;
import org.springframework.ui.Model;

import java.util.List;

public interface UserService {
    void addUser(String username, String password);

    void deleteUser(String username);

    User getUser(String username);

    List<User> getAllUsers();

    void updateUser(String username, String role, String password, String playerId);

    List<User> getUsersByName(String name);

    void addUserAndPlayerToModel (String username, Model model);

    boolean userExists(String username);

}
