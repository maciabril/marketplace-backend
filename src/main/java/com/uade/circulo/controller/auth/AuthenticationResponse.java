package com.uade.circulo.controller.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uade.circulo.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("role")
    private Role role;
}
