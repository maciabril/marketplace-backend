package com.uade.circulo.service;

import com.uade.circulo.entity.User;
import com.uade.circulo.enums.Role;
import com.uade.circulo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User userDetails) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        // Solo el admin puede editar cualquier usuario, el user solo el suyo
        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(id)) {
            throw new RuntimeException("No tienes permisos para editar este usuario");
        }

        // Verifica unicidad de email
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            userRepository.findByEmail(userDetails.getEmail())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> { throw new RuntimeException("El email ya está en uso por otro usuario"); });
            user.setEmail(userDetails.getEmail());
        }

        // Verifica unicidad de username
        if (userDetails.getName() != null && !userDetails.getName().equals(user.getName())) {
            userRepository.findAll().stream()
                .filter(u -> u.getName().equals(userDetails.getName()) && !u.getId().equals(id))
                .findAny()
                .ifPresent(u -> { throw new RuntimeException("El username ya está en uso por otro usuario"); });
            user.setUsername(userDetails.getName());
        }

        if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
}