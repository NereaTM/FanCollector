package com.svalero.fancollector.service;

import com.svalero.fancollector.dto.ColeccionInDTO;
import com.svalero.fancollector.dto.ColeccionOutDTO;
import com.svalero.fancollector.dto.ColeccionPutDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.exception.domain.UsuarioNoEncontradoException;

import java.util.List;

public interface ColeccionService {

    ColeccionOutDTO crearColeccion(ColeccionInDTO datosColeccion)
            throws UsuarioNoEncontradoException;

    ColeccionOutDTO buscarColeccionPorId(Long idColeccion)
            throws ColeccionNoEncontradaException;

    List<ColeccionOutDTO> listarColecciones(String nombreColeccion, String categoriaColeccion, Long idCreador, String nombreCreador);

    ColeccionOutDTO actualizarColeccion(Long idColeccion, ColeccionPutDTO datosColeccion)
            throws ColeccionNoEncontradaException;

    void eliminarColeccion(Long idColeccion)
            throws ColeccionNoEncontradaException;

    ColeccionOutDTO actualizarEsPublica(Long id, Boolean esPublica)
            throws ColeccionNoEncontradaException;

    ColeccionOutDTO actualizarUsableComoPlantilla(Long id, Boolean usableComoPlantilla)
            throws ColeccionNoEncontradaException;
}
