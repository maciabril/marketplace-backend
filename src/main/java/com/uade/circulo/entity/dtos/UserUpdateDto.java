package com.uade.circulo.entity.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class UserUpdateDto {
    private String name;
    private String password;
    private String email;
}
