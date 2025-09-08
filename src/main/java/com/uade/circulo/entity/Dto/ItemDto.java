package com.uade.circulo.entity.Dto;

import com.uade.circulo.enums.Status;
import jakarta.persistence.Column;

public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private Status status;
    private int stock;
    private int discount;
}
//todo: falta implementarlo en este archivo el get, no lo borren !! desp lo arreglo, no rompe ni tiene usos AUN atte abirl :p