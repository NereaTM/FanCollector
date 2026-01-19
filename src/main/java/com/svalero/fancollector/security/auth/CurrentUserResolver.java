package com.svalero.fancollector.security.auth;

import com.svalero.fancollector.domain.Usuario;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;
import com.svalero.fancollector.exception.security.AccesoDenegadoException;
import com.svalero.fancollector.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserResolver {

    private final UsuarioRepository usuarioRepository;

    public CurrentUserResolver(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario usuarioActual(String emailUsuario) {
        if (emailUsuario == null || emailUsuario.isBlank()) {
            throw new AccesoDenegadoException("Usuario no autenticado");
        }
        return usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> UsuarioNoEncontradoException.porEmail(emailUsuario));

    }
}
