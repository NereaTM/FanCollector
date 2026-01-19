package com.svalero.fancollector.exception.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccesoDenegadoException extends RuntimeException {

    public AccesoDenegadoException() {super("No tienes permisos");}

    public AccesoDenegadoException(String message) {super(message);}
}
