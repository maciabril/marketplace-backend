package com.uade.circulo.controller;

import com.uade.circulo.entity.User;
import com.uade.circulo.entity.dtos.UserUpdateDto;
import com.uade.circulo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Page<User> users = userService.getAllUsers(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getCurrentUserProfile() {
        User user = userService.getCurrentUserProfile();
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto) {
        userService.updateUser(id, userUpdateDto);
        return ResponseEntity.ok().build();
    }
}