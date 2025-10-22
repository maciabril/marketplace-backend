package com.uade.circulo.service;

import com.uade.circulo.entity.User;
import com.uade.circulo.entity.dtos.UserUpdateDto;
import com.uade.circulo.enums.Role;
import com.uade.circulo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<User> getAllUsers(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void updateUser(Long id, UserUpdateDto userUpdateDto) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        // Solo el admin puede editar cualquier usuario, el user solo el suyo
        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(id)) {
            throw new RuntimeException("No tienes permisos para editar este usuario");
        }

        // Verifica unicidad de email
        if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().equals(user.getEmail())) {
            userRepository.findByEmail(userUpdateDto.getEmail())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> { throw new RuntimeException("El email ya está en uso por otro usuario"); });
            user.setEmail(userUpdateDto.getEmail());
        }

        // Verifica unicidad de username
        if (userUpdateDto.getName() != null && !userUpdateDto.getName().equals(user.getName())) {
            userRepository.findAll().stream()
                .filter(u -> u.getName().equals(userUpdateDto.getName()) && !u.getId().equals(id))
                .findAny()
                .ifPresent(u -> { throw new RuntimeException("El username ya está en uso por otro usuario"); });
            user.setUsername(userUpdateDto.getName());
        }

        if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }

        userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getCurrentUserProfile() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
}