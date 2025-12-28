package com.svalero.fancollector.exception.domain;

public class UsuarioColeccionNoEncontradoException extends RuntimeException {

    public UsuarioColeccionNoEncontradoException(Long id) {
        super("Usuario-colección con ID: " + id + "no encontrado");
    }
}