package com.uade.circulo.entity.dto;

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
}

//todo: falta implementarlo en este archivo el get, no lo borren !! desp lo arreglo, no rompe ni tiene usos AUN atte abirl :p