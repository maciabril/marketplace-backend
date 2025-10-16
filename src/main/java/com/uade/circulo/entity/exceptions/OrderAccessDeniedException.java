package com.uade.circulo.entity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class OrderAccessDeniedException extends RuntimeException {
    public OrderAccessDeniedException(Long id) {
        super("No tiene permiso para ver la orden solicitada con ID: " + id);
    }
        public OrderAccessDeniedException(String message) {
        super("Acceso a Orden denegado: " + message);
    }
}
