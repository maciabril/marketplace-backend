package com.uade.circulo.entity.dtos;

import com.uade.circulo.enums.Category;
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
    private Double price;
    private Integer discount;
    private Integer stock;
    private Category category;
    private String imageName;
    private String imageData;
}

