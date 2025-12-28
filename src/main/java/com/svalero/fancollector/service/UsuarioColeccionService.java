package com.svalero.fancollector.service;

import com.svalero.fancollector.dto.UsuarioColeccionInDTO;
import com.svalero.fancollector.dto.UsuarioColeccionOutDTO;
import com.svalero.fancollector.dto.UsuarioColeccionPutDTO;
import com.svalero.fancollector.dto.patches.UsuarioColeccionFavoritaDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.exception.domain.UsuarioColeccionNoEncontradoException;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;

import java.util.List;

public interface UsuarioColeccionService {

    UsuarioColeccionOutDTO crear(UsuarioColeccionInDTO dto)
            throws UsuarioNoEncontradoException, ColeccionNoEncontradaException;

    UsuarioColeccionOutDTO buscarPorId(Long id)
            throws UsuarioColeccionNoEncontradoException;

    List<UsuarioColeccionOutDTO> listar(Long idUsuario, Long idColeccion, Boolean soloFavoritas);

    UsuarioColeccionOutDTO actualizar(Long id, UsuarioColeccionPutDTO dto)
            throws UsuarioColeccionNoEncontradoException;

    void eliminar(Long id)
           throws UsuarioColeccionNoEncontradoException;


    UsuarioColeccionOutDTO actualizarFavorita(Long id, UsuarioColeccionFavoritaDTO dto)
            throws UsuarioColeccionNoEncontradoException;
}
