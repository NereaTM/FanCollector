package com.svalero.fancollector.exception.domain;

public class UsuarioNoEncontradoException extends RuntimeException {

    public UsuarioNoEncontradoException(Long id) {
        super("Usuario con id " + id + " no encontrado");
    }


}
