package com.uade.circulo.entity.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)  // <-- Esto hace que devuelva 400 automáticamente
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
