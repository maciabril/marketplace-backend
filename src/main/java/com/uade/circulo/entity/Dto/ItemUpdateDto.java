package com.uade.circulo.entity.dto;

import com.uade.circulo.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemUpdateDto {
    private String name;
    private String description;
    private Status status; 
    private Integer discount;
    private Integer stock;
}

