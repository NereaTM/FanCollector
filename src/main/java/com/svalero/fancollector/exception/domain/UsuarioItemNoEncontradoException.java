package com.svalero.fancollector.exception.domain;

public class UsuarioItemNoEncontradoException extends RuntimeException {

    public UsuarioItemNoEncontradoException(Long id) {
        super("Usuario-Item con id " + id + " no encontrado");
    }
}