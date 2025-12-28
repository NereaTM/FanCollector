package com.svalero.fancollector.service;

import com.svalero.fancollector.domain.enums.EstadoItem;
import com.svalero.fancollector.dto.UsuarioItemInDTO;
import com.svalero.fancollector.dto.UsuarioItemOutDTO;
import com.svalero.fancollector.dto.UsuarioItemPutDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.exception.domain.ItemNoEncontradoException;
import com.svalero.fancollector.exception.domain.UsuarioItemNoEncontradoException;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;

import java.util.List;

public interface UsuarioItemService {

    UsuarioItemOutDTO crear(UsuarioItemInDTO dto)
            throws UsuarioNoEncontradoException, ItemNoEncontradoException, ColeccionNoEncontradaException;

    UsuarioItemOutDTO buscarPorId(Long id) throws UsuarioItemNoEncontradoException;

    List<UsuarioItemOutDTO> listar(
            Long idUsuario, Long idItem, Long idColeccion, EstadoItem estado, Boolean esVisible);

    UsuarioItemOutDTO actualizarCompleto(Long id, UsuarioItemPutDTO dto)
            throws UsuarioItemNoEncontradoException;

    UsuarioItemOutDTO actualizarVisibilidad(Long id, Boolean esVisible)
            throws UsuarioItemNoEncontradoException;

    void eliminar(Long id) throws UsuarioItemNoEncontradoException;
}
