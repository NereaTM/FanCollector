package com.svalero.fancollector.exception.validation;

public class EmailDuplicadoException extends RuntimeException {

    public EmailDuplicadoException(String email) {
        super("El email " + email + " ya está registrado");
    }
}