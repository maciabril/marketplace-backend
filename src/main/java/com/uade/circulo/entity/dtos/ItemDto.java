package com.uade.circulo.entity.dtos;

import com.uade.circulo.enums.Category;
import com.uade.circulo.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private double price; //precio con descuento
    private Status status;
    private int stock;
    private int discount;
    private Category category;
    private String imageName;
    private byte[] imageData;
}

