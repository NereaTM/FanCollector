package com.svalero.fancollector.service;

import com.svalero.fancollector.domain.enums.RarezaItem;
import com.svalero.fancollector.dto.ItemInDTO;
import com.svalero.fancollector.dto.ItemOutDTO;
import com.svalero.fancollector.dto.ItemPutDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.exception.domain.ItemNoEncontradoException;

import java.util.List;

public interface ItemService {

    ItemOutDTO crearItem(ItemInDTO datosItem) throws ColeccionNoEncontradaException;

    ItemOutDTO buscarItemPorId(Long idItem) throws ItemNoEncontradoException;

    List<ItemOutDTO> listarItems(String nombre, String tipo, String rareza, Long idColeccion);

    ItemOutDTO actualizarItem(Long idItem, ItemPutDTO datosItem) throws ItemNoEncontradoException;

    void eliminarItem(Long idItem) throws ItemNoEncontradoException;

    ItemOutDTO actualizarRareza(Long id, RarezaItem rareza) throws ItemNoEncontradoException;
}
