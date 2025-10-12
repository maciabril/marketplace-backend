package com.uade.circulo.entity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Orden no encontrada con ID: " + id);
    }

    public OrderNotFoundException(String message) {
        super("Orden no encontrada:" + message);
    }
}