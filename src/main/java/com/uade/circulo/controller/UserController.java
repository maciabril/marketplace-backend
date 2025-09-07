package com.uade.circulo.controller;

import com.uade.circulo.entity.User;
import com.uade.circulo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    // @PostMapping("/register")
    // public ResponseEntity<User> registerUser(@RequestBody User user) {
    //     try {
    //         User newUser = userService.registerUser(user);
    //         return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    //     } catch (RuntimeException e) {
    //         // Manejar error de username duplicado
    //         return new ResponseEntity<>(HttpStatus.CONFLICT);
    //     }
    // }

    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody User loginRequest) {
    //     try {
    //         User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
    //         return ResponseEntity.ok(user);
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    //     }
    // }
}