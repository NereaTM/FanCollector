package com.svalero.fancollector.service;

import com.svalero.fancollector.dto.UsuarioColeccionInDTO;
import com.svalero.fancollector.dto.UsuarioColeccionOutDTO;
import com.svalero.fancollector.dto.UsuarioColeccionPutDTO;
import com.svalero.fancollector.dto.patches.UsuarioColeccionFavoritaDTO;
import com.svalero.fancollector.dto.patches.UsuarioColeccionVisibleDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.exception.domain.UsuarioColeccionNoEncontradoException;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;

import java.util.List;

public interface UsuarioColeccionService {

    UsuarioColeccionOutDTO crear(UsuarioColeccionInDTO dto, String emailUsuario, boolean esAdmin, boolean esMods)
            throws UsuarioNoEncontradoException, ColeccionNoEncontradaException;

    UsuarioColeccionOutDTO buscarPorId(Long id, String emailUsuario, boolean esAdmin, boolean esMods)
            throws UsuarioColeccionNoEncontradoException;

    List<UsuarioColeccionOutDTO> listar(Long idUsuario, Long idColeccion, Boolean soloFavoritas, Boolean esVisible, String emailUsuario, boolean esAdmin, boolean esMods);

    UsuarioColeccionOutDTO actualizar(Long id, UsuarioColeccionPutDTO dto, String emailUsuario, boolean esAdmin, boolean esMods)
            throws UsuarioColeccionNoEncontradoException;

    void eliminar(Long id, String emailUsuario, boolean esAdmin, boolean esMods)
            throws UsuarioColeccionNoEncontradoException;

    UsuarioColeccionOutDTO actualizarFavorita(Long id, UsuarioColeccionFavoritaDTO dto, String emailUsuario, boolean esAdmin, boolean esMods)
            throws UsuarioColeccionNoEncontradoException;

    UsuarioColeccionOutDTO actualizarVisible(Long id, UsuarioColeccionVisibleDTO dto, String emailUsuario, boolean esAdmin, boolean esMods)
            throws UsuarioColeccionNoEncontradoException;
}
