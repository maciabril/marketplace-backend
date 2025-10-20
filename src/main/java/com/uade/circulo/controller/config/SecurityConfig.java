package com.uade.circulo.controller.config;

import com.uade.circulo.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        // Auth endpoints (públicos)
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        
                        // Catálogo de productos (público)
                        .requestMatchers(HttpMethod.GET, "/api/item/catalog/products").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/item/catalog/products/{id}").permitAll()

                        // Productos (solo admin/vendedor)
                        .requestMatchers(HttpMethod.POST, "/api/item/products").hasAuthority(Role.ADMIN.name()) // crear producto
                        .requestMatchers(HttpMethod.PUT, "/api/item/products/{id}").hasAuthority(Role.ADMIN.name()) // editar producto
                        .requestMatchers(HttpMethod.DELETE, "/api/item/products/{id}").hasAuthority(Role.ADMIN.name()) // borrar producto

                        // Órdenes
                        .requestMatchers(HttpMethod.GET, "/api/action/orders").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name()) // ver todas las órdenes
                        .requestMatchers(HttpMethod.POST, "/api/action/orders").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name()) // crear orden
                        .requestMatchers(HttpMethod.GET, "/api/action/orders/{id}").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name()) // ver orden por id

                        // Usuarios
                        .requestMatchers(HttpMethod.GET, "/api/user/users").hasAuthority(Role.ADMIN.name()) // ver todos los usuarios
                        .requestMatchers(HttpMethod.GET, "/api/user/users/{id}").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name()) // ver usuario por id
                        .requestMatchers(HttpMethod.PUT, "/api/user/users/update/{id}").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name()) // actualizar usuario
                
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
