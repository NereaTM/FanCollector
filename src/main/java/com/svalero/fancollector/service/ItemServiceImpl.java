package com.svalero.fancollector.service;

import com.svalero.fancollector.domain.Coleccion;
import com.svalero.fancollector.domain.Item;
import com.svalero.fancollector.domain.enums.RarezaItem;
import com.svalero.fancollector.dto.ItemInDTO;
import com.svalero.fancollector.dto.ItemOutDTO;
import com.svalero.fancollector.dto.ItemPutDTO;
import com.svalero.fancollector.exception.domain.ColeccionNoEncontradaException;
import com.svalero.fancollector.exception.domain.ItemNoEncontradoException;
import com.svalero.fancollector.repository.ColeccionRepository;
import com.svalero.fancollector.repository.ItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ColeccionRepository coleccionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ItemOutDTO crearItem(ItemInDTO datosItem) throws ColeccionNoEncontradaException {

        Coleccion coleccion = coleccionRepository.findById(datosItem.getIdColeccion())
                .orElseThrow(() -> new ColeccionNoEncontradaException(datosItem.getIdColeccion()));

        Item item = modelMapper.map(datosItem, Item.class);

        if (datosItem.getRareza() != null) {
            try {RarezaItem rareza = RarezaItem.valueOf(datosItem.getRareza().toUpperCase());
                item.setRareza(rareza);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Rareza no válida: " + datosItem.getRareza());
            }
        }

        item.setColeccion(coleccion);

        Item guardado = itemRepository.save(item);
        return modelMapper.map(guardado, ItemOutDTO.class);
    }

    @Override
    public ItemOutDTO buscarItemPorId(Long idItem) throws ItemNoEncontradoException {
        Item item = itemRepository.findById(idItem)
                .orElseThrow(() -> new ItemNoEncontradoException(idItem));
        return modelMapper.map(item, ItemOutDTO.class);
    }

    @Override
    public List<ItemOutDTO> listarItems(String nombre, String tipo, String rarezaStr, Long idColeccion) {

        List<Item> items;
        RarezaItem rareza = null;

        if (rarezaStr != null && !rarezaStr.isBlank()) {
            try { rareza = RarezaItem.valueOf(rarezaStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de rareza no válido: " + rarezaStr);
            }
        }

        if ((nombre == null || nombre.isBlank()) &&
                (tipo == null || tipo.isBlank()) &&
                (rarezaStr == null || rarezaStr.isBlank()) &&
                idColeccion == null) {
            items = itemRepository.findAll();
        } else {
            items = itemRepository.buscarPorFiltros(nombre, tipo, rareza, idColeccion);
        }

        List<ItemOutDTO> resultado = new ArrayList<>();
        for (Item item : items) {resultado.add(modelMapper.map(item, ItemOutDTO.class));}
        return resultado;
    }

    @Override
    public ItemOutDTO actualizarItem(Long idItem, ItemPutDTO datosItem)
            throws ItemNoEncontradoException {

        Item existente = itemRepository.findById(idItem)
                .orElseThrow(() -> new ItemNoEncontradoException(idItem));

        existente.setNombre(datosItem.getNombre());
        existente.setDescripcion(datosItem.getDescripcion());
        existente.setTipo(datosItem.getTipo());
        existente.setImagenUrl(datosItem.getImagenUrl());
        existente.setAnioLanzamiento(datosItem.getAnioLanzamiento());

        if (datosItem.getRareza() != null && !datosItem.getRareza().isBlank()) {
            try {
                RarezaItem rareza = RarezaItem.valueOf(datosItem.getRareza().toUpperCase());
                existente.setRareza(rareza);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Rareza no válida: " + datosItem.getRareza());
            }
        }

        Item actualizado = itemRepository.save(existente);
        return modelMapper.map(actualizado, ItemOutDTO.class);
    }

    @Override
    public ItemOutDTO actualizarRareza(Long id, RarezaItem rareza)
            throws ItemNoEncontradoException {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNoEncontradoException(id));

        item.setRareza(rareza);

        Item actualizado = itemRepository.save(item);
        return modelMapper.map(actualizado, ItemOutDTO.class);
    }

    @Override
    public void eliminarItem(Long idItem)
            throws ItemNoEncontradoException {

        Item item = itemRepository.findById(idItem)
                .orElseThrow(() -> new ItemNoEncontradoException(idItem));

        itemRepository.delete(item);
    }
}
