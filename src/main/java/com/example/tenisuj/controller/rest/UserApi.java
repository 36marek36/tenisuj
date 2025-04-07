package com.example.tenisuj.controller.rest;

import com.example.tenisuj.controller.rest.request.CreateUser;
import com.example.tenisuj.controller.rest.request.UpdateUser;
import com.example.tenisuj.model.User;
import com.example.tenisuj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/users")
public class UserApi {
    private final UserService userService;

    @Autowired
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    List<User> getUsers() {
        log.info("Get all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    User getUser(@PathVariable("id") String id) {
        log.info("Get user with name: {}", id);
        return userService.getUser(id);
    }

    @GetMapping("/!find")
    List<User> findUsersByName(@RequestParam("name") String name) {
        log.info("Find user by name: {}", name);
        return userService.getUsersByName(name);
    }

    @GetMapping("/player/!find")
    User findUserByPlayerId(@RequestParam("playerId") String playerId) {
        log.info("Find user by playerId: {}", playerId);
        return userService.getUserByPlayerId(playerId);
    }

    @PostMapping("/create")
    ResponseEntity<String> createUser(@RequestBody CreateUser user) {
        userService.addUser(user.username(), user.password());
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    ResponseEntity<String> updateUser(@PathVariable("id") String id, @RequestBody UpdateUser user) {
        userService.updateUser(id,user.role(),user.password(), user.playerId());
        return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error("UsernameNotFoundException: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
