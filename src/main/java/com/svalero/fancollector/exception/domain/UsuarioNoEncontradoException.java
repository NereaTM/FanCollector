package com.svalero.fancollector.exception.domain;

public class UsuarioNoEncontradoException extends RuntimeException {

    public UsuarioNoEncontradoException(Long id) {
        super("Usuario con id " + id + " no encontrado");
    }

    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public static UsuarioNoEncontradoException porEmail(String email) {
        return new UsuarioNoEncontradoException(email + " no encontrado");
    }
}
