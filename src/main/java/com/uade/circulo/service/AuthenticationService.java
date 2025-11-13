package com.uade.circulo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.circulo.controller.auth.AuthenticationRequest;
import com.uade.circulo.controller.auth.AuthenticationResponse;
import com.uade.circulo.controller.auth.RegisterRequest;
import com.uade.circulo.controller.config.JwtService;
import com.uade.circulo.entity.User;
import com.uade.circulo.entity.exceptions.BadRequestException;
import com.uade.circulo.repository.UserRepository;
import com.uade.circulo.enums.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                if (repository.findByEmail(request.getEmail()).isPresent()) {
                        throw new BadRequestException("El email ya está registrado");
                }
                if (repository.findByUsername(request.getUsername()).isPresent()) {
                        throw new BadRequestException("El nombre de usuario ya está en uso");
                }

                var user = User.builder()
                                .firstName(request.getFirstname())
                                .lastName(request.getLastname())
                                .email(request.getEmail())
                                .username(request.getUsername())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(Role.USER)
                                .build();

                repository.save(user);
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                throw new BadRequestException("El email no puede estar vacío.");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                throw new BadRequestException("La contraseña no puede estar vacía.");
        }

        var userOptional = repository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
                throw new BadRequestException("Mail o contraseña incorrectos."); 
        }

        try {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
        } catch (BadCredentialsException e) {
                throw new BadRequestException("Mail o contraseña incorrectos.");
        }
        var user = userOptional.get();
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .role(user.getRole())
                .id(user.getId())
                .build();
        }
}
